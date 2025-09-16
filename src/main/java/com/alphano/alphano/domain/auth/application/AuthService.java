package com.alphano.alphano.domain.auth.application;

import com.alphano.alphano.domain.auth.dto.SignupRequest;
import com.alphano.alphano.domain.auth.exception.IdentifierAlreadyExistsException;
import com.alphano.alphano.domain.user.dao.UserRepository;
import com.alphano.alphano.domain.user.domain.Role;
import com.alphano.alphano.domain.user.domain.User;
import com.alphano.alphano.domain.user.dto.UserInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     * @param request
     * @return
     */
    @Transactional
    public UserInfoResponse signup(@Valid SignupRequest request) {
        if (userRepository.findByIdentifier(request.identifier()).isPresent()) {
            throw IdentifierAlreadyExistsException.EXCEPTION;
        }
        User user = User.builder()
                .identifier(request.identifier())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);
        return UserInfoResponse.from(saved);
    }
}
