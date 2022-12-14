package com.bitstudy.app.domain;



/* 할 일 : Lombok 사용하기
 *  주의 : maven 때랑 같은 방식인 것들도 이름이 다르게 되어 있으니 헷갈리지 않게 주의
 *
 *  순서
 *  1) Lombok 을 이용해서 클래스를 엔티티로 변경 @Entity
 *  2) getter/setter, toString 등의 Lombok annotation 사용
 *  3) 동등성, 동일성 비교 할 수 있는 코드 넣어볼 예정
 * */


/** Article과 ArticleComment에 있는 공통필드(메타데이터, id는 제외)들을 별도로 빼서 관리할것임.
 *  이유는 앞으로 Article과 ArticleComment 처럼 fk 같은거로 엮여있는 테이블들 만들경우,
 *  모든  domain 안에있는 파일들에 많은 중복코드들이 들어가게 된다.
 *  즉, {별도의 파일에 공통되는 것들을 다 몰아넣고 사용하는거 해볼것!}
 *
 *  참고: 공통필드를 뺴는건 팀마다 다르다.
 *       중복코드를 싫어해서 그냥 각 파일마다 다 두는 사람들이 있고,
 *       (장: 유지보수 , 단: 난이도 상승)
 *       중복코드를 괜찮아해서 각 파일에 그냥 두는 사람도 있음~
 *       (각 파일에 모든 정보 들어있어서, 변경시 유연하게 코드 작업 가능~)
 *
 *추출은 두가지 방법으로 할 수 있다.
 * 1) @Embedded - 공통되는 필드들을 하나의 클래스로 만들어서 @Embedded 있는 곳에서 치환 하는 방식
 *
 * 2) @MapperSupperClass - (요즘 실무에서 사용)
 *                  @MapperSupperClass 어노테이션이 붙은 곳에서 사용
 *
 *  * 둘의 차이 : 사실은 둘이 비슷하지만 @Embedded 방식을 하게 되면 필드가 하나 추가되고, 영속성 컨텍스트를 통해서
 *  데이터를 넘겨받아서 어플리케이션으로 열었을때에는 어차피 AuditingField랑 똑같이 보인다.
 *  (중간에 한단계가 더 있다는 뜻)
 *          
 *          @MapperSupperClass는 표준 JPA에서 제공해주는 클래스. 중간단계 따로 없이 바로 동작
 *
 *
 *
 * */
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** @Table - 엔티티와 매핑할 정보를 지정하고
사용법 : @Index(name ="원하는 명칭", columnList = "사용할 테이블명")
name 부분을 생략하면 원래 이름 사용.

 @Index - 데이터베이스 인덱스는 추가, 쓰기 및 저장 공간을 희생해서 테이블에 대한 데이터 검색 속도를 향상시키는 데이터 구조
 사용법 : @Entity 와 세트로 사용

 */

@Table(indexes = {
        @Index(columnList = "title"),  // 검색속도 빠르게 해주는 작업
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
//@EntityListeners(AuditingEntityListener.class)/* 이거 없으면 테스트 할때 createAt 때문에 에러남*/
@Entity // Lombok 을 이용해서 클래스를 엔티티로 변경 @Entity 가 붙은 클래스는 JPA 가 관리하게 된다.
@Getter // 모든 필드의 getter 들이 생성
@ToString // 모든 필드의 toString 생성
public class Article extends AuditingFields{
    
    /*extends 한 이유 : 인터페이스는 선언문만있어서 사용하기에 부적합*/

    @Id // 전체 필드중에서 PK 표시 해주는 것 @Id 가 없으면 @Entity 어노테이션을 사용 못함
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 필드가 auto_increment 인 경우 @GeneratedValue 를 써서 자동으로 값이 생성되게 해줘야 한다. (기본키 전략)
    private long id; // 게시글 고유 아이디

    /*
      @Setter 도 @Getter 처럼 클래스 단위로 걸 수 있는데, 그렇게 하면 모든 필드에 접근이 가능해진다.
      그런데 id 같은 경우에는 내가 부여하는게 아니라 JPA 에서 자동으로 부여해주는 번호이다.
      메타 데이터들도 자동으로 JPA 가 세팅 되게 만들어야 한다. 그래서 id 와 메타데이터는 @Setter 가 필요 없다.
      @Setter 의 경우에는 지금처럼 필요한 필드에만 주는걸 연습하자.(요건 강사님 스타일)

    */

    /**
     @Column - 해당 컬럼이 not null 인 경우 @Column(nullable =false) 써준다.
     기본 값은 true 라서 @Column 을 아예 안쓰면 null 가능
     사용법 : @Column(nullable = false, length = 숫자) 숫자 안쓰면 기본 값 255 적용
     */
    @Setter
    @Column(nullable = false)
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    @Setter
    private String hashtag; // 해시태그

    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    /* 1) Embeded방식 - 예시는 강사님거 붙여넣자!*/
//    //메타데이터
//    @CreatedDate
//    @Column(nullable = false)
//    private LocalDateTime createdAt; // 생성일자
//
//    @CreatedBy
//    @Column(nullable = false,length = 100)
//    private String createdBy; // 생성자
//
//    @LastModifiedDate
//    @Column(nullable = false)
//    private LocalDateTime modifiedAt; // 수정일자
//
//    @LastModifiedBy
//    @Column(nullable = false,length = 100)
//    private String modifiedBy; // 수정자
    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag){
        return new Article(title,content,hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
