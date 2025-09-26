package com.alphano.alphano.domain.auth.api;

import com.alphano.alphano.common.application.MailVerificationService;
import com.alphano.alphano.common.dto.request.EmailSendRequest;
import com.alphano.alphano.common.dto.request.EmailVerificationRequest;
import com.alphano.alphano.common.dto.response.EmailVerificationResponse;
import com.alphano.alphano.domain.auth.application.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/email")
@RequiredArgsConstructor
public class AuthEmailController {
    private final AuthService authService;
    private final MailVerificationService mailVerificationService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@Valid @RequestBody EmailSendRequest request) {
        mailVerificationService.sendCodeForSignUp(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<EmailVerificationResponse> verify(@Valid @RequestBody EmailVerificationRequest request) {
        var token = mailVerificationService.verifyCode(request.email(), request.code());
        return ResponseEntity.ok(new EmailVerificationResponse(token.value(), token.ttlSeconds()));
    }
}
