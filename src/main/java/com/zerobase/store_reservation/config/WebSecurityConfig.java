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
                                .anyRequest().permitAll() // 모든 요청을 허용
                )
                .formLogin((form) -> form
                        .loginProcessingUrl("/login") // 기본 로그인 엔드포인트로 변경
                        // 로그인 성공 시 응답 메시지를 "login success"로 반환.
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().write("login success");
                        })
                        // 로그인 실패 시 "login fail" 메시지를 반환.
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write("login fail");
                        })
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll // 모든 사용자가 로그아웃 가능하도록 허용.
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 암호화, 사용자의 비밀번호를 해싱하여 저장하도록 설정.(BCryptPasswordEncoder는 보안성이 높은 해싱 알고리즘을 제공.)
        return new BCryptPasswordEncoder();
    }
}

