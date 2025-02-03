package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.SignUpRequestDto;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.repository.UserRepository;
import com.zerobase.store_reservation.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // Partner 인증 토큰
    @Value("${app.partner-token}")
    private String partnerToken; // application.properties 에서 값을 가져옴


    public void signUp(SignUpRequestDto signUpRequestDto) {
        // 회원가입을 할 때 이미 해당 ID 가 있다면 예외발생
        Optional<User> existByName = userRepository.findByName(signUpRequestDto.getUsername());
        if (existByName.isPresent()) {
            throw new IllegalStateException("중복된 이름이 있습니다.");
        }

        String name = signUpRequestDto.getUsername();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());
        String email = signUpRequestDto.getEmail();

        // 기본으로는 일반 user
        UserRole role = UserRole.USER;
        // Partner 이고 함께 넣어준 토큰 값이 일치한다면 Partner role 부여
        if (signUpRequestDto.isPartner()) {
            if (signUpRequestDto.getPartnerToken().equals(partnerToken)) {
                role = UserRole.PARTNER;
            } else {
                throw new RuntimeException("토큰 값이 일치하지 않습니다.");
            }
        }
        User user = new User(name, password, email, role);
        userRepository.save(user);
    }

}
