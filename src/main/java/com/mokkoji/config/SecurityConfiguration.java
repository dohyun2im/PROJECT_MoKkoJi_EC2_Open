package com.mokkoji.config;

import com.mokkoji.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .logout()
                .logoutSuccessUrl("/")                         // 로그아웃 성공 시 URL
                .logoutRequestMatcher( new AntPathRequestMatcher("/logout")) // 로그아웃 요청 주소 정의 - 이 주소를 요청하면 로그아웃 시켜줌
                .invalidateHttpSession(true)                   // 세션 초기화
                .and()
                .csrf() // 요청 위조 방지
                .ignoringAntMatchers("/board/setboard")
                .ignoringAntMatchers("/board/deleteboard")
                .ignoringAntMatchers("/board/setreply")
                .ignoringAntMatchers("/board/deletereply")
                .ignoringAntMatchers("/member/getmno")
                .ignoringAntMatchers("/board/setlikes")
                .ignoringAntMatchers("/board/updateBview")
                .ignoringAntMatchers("/board/getboardoptions")
                .and()
                .oauth2Login()                                 // 소셜 로그인 보안 설정
                .defaultSuccessUrl("/")                        // 소셜 로그인 성공 시 이동하는 라우터
                .userInfoEndpoint()                            // Endpoint : 종착점 - 소셜 회원 정보를 받는 곳
                .userService(memberService);                   // 해당 서비스 loadUser 구현
    }
}
