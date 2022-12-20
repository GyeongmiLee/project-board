package com.bitstudy.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/*@Configuration(배열이란뜻) 이라고 하면 설정파일을 만들기 위한 annotation or Bean을 등록하기 위한 annotation
    이걸 쓰면 JpaConfig 클래스는 Configuration bean이 된다.
    간단히 말해서 @Configuration 달아놓으면 시스템이 "이거 설정파일이야! Bean으로 등록해줘!라는 뜻!"
    auditing(감시라는뜻!)
*
* */
@Configuration
/*JPA에서 auditing을 가능하게 하는 annotation
* JpaAuditing이란 :Spring Data jpa에서 자동으로 값을 넣어주는 기능
*                 jpa에서 자동으로 세팅하게 해줄 때 사용하는 기능인데, 특정 데이터를 보고 있다가 생성이되거나
*                 수정되면 자동으로 값을 넣어주는 기능*/
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware(){
        return ()-> Optional.of("bitstudy");
        // 이렇게 하면 앞으로 JPA Audition 할때마다 사람이름은 이걸로 넣게 된다~
        // TODO: 나중에 스프링 시큐리티로 인증기능 붙일 때, 수정 필요.
    }

}
