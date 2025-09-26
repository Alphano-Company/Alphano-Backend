package com.alphano.alphano.domain.auth.api;

import com.alphano.alphano.domain.auth.application.AuthService;
import com.alphano.alphano.domain.auth.dto.request.LoginRequest;
import com.alphano.alphano.domain.auth.dto.request.SignupRequest;
import com.alphano.alphano.domain.user.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입")
    ResponseEntity<UserInfoResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    ResponseEntity<UserInfoResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    /*
    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급")
    ResponseEntity<> refresh(

    )
    */
}
