package com.alphano.alphano.domain.auth.application;

import com.alphano.alphano.domain.auth.dto.LoginRequest;
import com.alphano.alphano.domain.auth.dto.SignupRequest;
import com.alphano.alphano.domain.auth.exception.IdentifierAlreadyExistsException;
import com.alphano.alphano.domain.auth.exception.InvalidPasswordException;
import com.alphano.alphano.domain.user.dao.UserRepository;
import com.alphano.alphano.domain.user.domain.Role;
import com.alphano.alphano.domain.user.domain.User;
import com.alphano.alphano.domain.user.dto.UserInfoResponse;
import com.alphano.alphano.security.exception.IdentifierNotFoundException;
import com.alphano.alphano.security.jwt.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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

        return issueTokens(saved);
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    @Transactional
    public UserInfoResponse login(@Valid LoginRequest request) {
        User user = userRepository.findByIdentifier(request.identifier())
                .orElseThrow(() -> IdentifierNotFoundException.EXCEPTION);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw InvalidPasswordException.EXCEPTION;
        }

        return issueTokens(user);
    }

    // ===== Helper method =====//
    private UserInfoResponse issueTokens(User user) {
        var roles = rolesOf(user);
        String accessToken = jwtProvider.createAccessToken(
                user.getId(),
                user.getIdentifier(),
                roles
        );
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        return UserInfoResponse.from(user, accessToken, refreshToken);
    }

    private List<String> rolesOf(User user) {
        return List.of(user.getRole().getValue());
    }
}
