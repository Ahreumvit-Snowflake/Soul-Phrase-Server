package com.ahreumvitsnowflake.graduation.springboot.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    //.mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers("http://ec2-54-180-180-137.ap-northeast-2.compute.amazonaws.com:8000/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "http://ec2-54-180-180-137.ap-northeast-2.compute.amazonaws.com:8080/login-check", "http://ec2-54-180-180-137.ap-northeast-2.compute.amazonaws.com:8080/test/**", "http://ec2-54-180-180-137.ap-northeast-2.compute.amazonaws.com:8080/api/v1/**").permitAll()
//                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("http://ec2-54-180-180-137.ap-northeast-2.compute.amazonaws.com:3000")
                .and()
                    .oauth2Login()
                    .defaultSuccessUrl("http://ec2-54-180-180-137.ap-northeast-2.compute.amazonaws.com:3000")
                    .userInfoEndpoint() // // OAuth2 로그인 성공 후 가져올 설정들
                    .userService(customOAuth2UserService); // 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
    }
}
