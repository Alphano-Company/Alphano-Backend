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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${jwt.expiration.access}")
    private long accessTtl;

    @Value("${jwt.expiration.refresh}")
    private long refreshTtl;

    /**
     * 회원 가입
     * @param request
     * @return
     */
    @Transactional
    public UserInfoResponse signup(SignupRequest request) {
        validateIdentifierUnique(request.identifier());

        User user = User.builder()
                .identifier(request.identifier())
                .password(passwordEncoder.encode(request.password()))
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
    public UserInfoResponse login(LoginRequest request) {
        User user = userRepository.findByIdentifier(request.identifier())
                .orElseThrow(() -> IdentifierNotFoundException.EXCEPTION);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw InvalidPasswordException.EXCEPTION;
        }

        return issueTokens(user);
    }

    // ===== Helper method =====//

    private void validateIdentifierUnique(String identifier) {
        if (userRepository.existsByIdentifier(identifier)) {
            throw IdentifierAlreadyExistsException.EXCEPTION;
        }
    }

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

    /*
    public AccessTokenResponse reissueAccessToken(String refreshToken) {
        Claims claims = jwtProvider.parseToken(refreshToken);

        String category = claims.get("category", String.class);
        if (!"reissue".equals(category)) {
            throw InvalidTokenException.EXCEPTION;
        }

        Long userId = Long.valueOf(claims.getSubject());
        String stored = ref
    }
     */
}
