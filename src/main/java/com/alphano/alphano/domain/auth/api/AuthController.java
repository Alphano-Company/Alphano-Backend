package com.alphano.alphano.domain.auth.api;

import com.alphano.alphano.domain.auth.application.AuthService;
import com.alphano.alphano.domain.auth.dto.SignupRequest;
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
}
