package com.eugeneprogram.config;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /*
     * 가장 기본적인 security 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  {
        http
            .authorizeHttpRequests((authz) -> authz
                    .anyRequest().authenticated())
            .httpBasic(withDefaults());
        return http.build();
    }
    */
    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("a","b").permitAll() // permitAll()로 인하여 "a","b" url에 대한 권한에 제한이 없다.
                                .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .formLogin(formLogin ->
                        formLogin
                                //.loginPage("/loginPage")    //내가 직접 설정한 로그인 페이지로 이동, 이 매써드가 없다면 시큐리티에서 제공한 로그인 페이지로 이동
                                .defaultSuccessUrl("/")     //시큐리티에서 제공하는 로그인 페이지에서 로그인 성공 시 괄호안의 url로 페이지 이동
                                .failureUrl("/login")   // 만약 실패한다면 login으로 이동, 그러나 해당 매써드가 없어도 login페이지로 이동한다.
                                .usernameParameter("userId")  //로그인 시 id의 name을 "userId"로 설정
                                .passwordParameter("passwd")    //로그인 시 pw의 name을 "passwd"로 설정
                                .loginProcessingUrl("/login_proc")  //form에서의 action을 login_proc로 설정
                                // 로그인 성공 시 해당 매써드 실행
                                .successHandler(new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        System.out.println("authentication : "+ authentication.getName());  //'authentication : 유저이름' 출력
                                        response.sendRedirect("/"); //인증 성공 후 root로 이동
                                    }
                                })
                                .failureHandler(new AuthenticationFailureHandler() {
                                    @Override
                                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                        System.out.println("exception : "+ exception.getMessage());
                                        response.sendRedirect("/login");    //인증 실패 후 login으로 이동
                                    }
                                })
                                .permitAll()
                );

        return http.build();
    }
}



