package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.SignUpRequestDto;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.repository.UserRepository;
import com.zerobase.store_reservation.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String PARTNER_TOKEN = "ABC123";


    public void signUp(SignUpRequestDto signUpRequestDto) {
        Optional<User> existByName = userRepository.findByName(signUpRequestDto.getUsername());
        if (existByName.isPresent()) {
            throw new IllegalStateException("중복된 이름이 있습니다.");
        }
        String name = signUpRequestDto.getUsername();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());
        String email = signUpRequestDto.getEmail();

        UserRole role = UserRole.USER;
        if (signUpRequestDto.isPartner()) {
            if (signUpRequestDto.getPartnerToken().equals(PARTNER_TOKEN)) {
                role = UserRole.PARTNER;
            } else {
                throw new RuntimeException("토큰 값이 일치하지 않습니다.");
            }
        }
        User user = new User(name, password, email, role);
        userRepository.save(user);
    }

}
