package com.zerobase.store_reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf 비활성화
                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/", "/home", "/register").permitAll() // 모든 사용자 접근 허용
//                        .requestMatchers("/partner/**").hasRole("PARTNER") // 파트너만 접근 가능
//                        .requestMatchers("/customer/**").hasRole("CUSTOMER") // 고객만 접근 가능
//                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                                .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginProcessingUrl("/login") // 기본 로그인 엔드포인트로 변경
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().write("login success");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write("login fail");
                        })
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll // 로그아웃 허용
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }
}

