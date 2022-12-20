package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity/* 테이블과의 매핑 한다는 뜻,
          JPA가 관리한다.
          PK가 알아볼 수 있게 필드들 중 하나에 @Id 어노테이션 달아줘야함*/
@Getter //순서 상관 없음
@ToString
@Table(indexes = {
        @Index(columnList="content"),
        @Index(columnList="createdAt"),
        @Index(columnList="createdBy")
})
public class Ex01_2_ArticleComment_엔티티로변경 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //article에 있는 코드를 끌어온것...-> 넘모 많다!
    //article과 매핑되어있는 요소들
    @Setter @ManyToOne(optional = false) private Article article; //다 불러와서
   // @Setter @Column(nullable = false) private Long articleId; // 이렇게 불러오면 좋은데
    // 못알아봄

    /** 연관관계 매핑
     *  연관관계 없이 만들면 private Long articleId: 이런식으로 (관계형 데이터베이스 스타일) 하면된다.
     *  그런데 우리는 Article과 ArticleComment 가 관계를 맺고 있는 걸 객체지향적으로 표현하려고 이렇게 쓸것임
     *  그러기 위해서 필요한건 단방향 이라는 뜻의 @ManyToOne annotation을 써주고,
     *  (optional = false) : 이건 필수값이다 라는 뜻
     *  "댓글은 여러개: 게시글 1개" 이기 때문에 단방향 방식을 썼다.
     *
     * **/
    @Setter
    @Column(nullable = false, length = 500)
    private String content; //본문
    
    //메타데이터
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일시

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy; //생성자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; //수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; //수정자
}
