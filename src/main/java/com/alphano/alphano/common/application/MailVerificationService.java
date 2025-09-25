package com.alphano.alphano.common.application;

import com.alphano.alphano.common.exception.InvalidOrExpiredException;
import com.alphano.alphano.common.exception.TooManyEmailRequestsException;
import com.alphano.alphano.common.infra.mail.MailTemplates;
import com.alphano.alphano.domain.auth.exception.EmailAlreadyExistsException;
import com.alphano.alphano.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MailVerificationService {

    private static final String KEY_CODE           = "auth:code:";            // email -> 6자리 코드
    private static final String KEY_THROTTLE_SEND  = "auth:throttle:send:";   // email -> 발송 횟수
    private static final String KEY_VERIFIED_TOKEN = "auth:verified-token:";  // token -> email
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    private final RedisService redis;
    private final MailService mailService;
    private final UserRepository userRepository;

    @Value("${auth.code.length:6}")                 private int  codeLen;                   // 인증코드 자리수
    @Value("${auth.code.ttl-seconds:300}")          private long ttlSeconds;                // 인증코드 유효시간 : 300초
    @Value("${auth.send.max-per-window:5}")         private int  maxPerWindow;              // 동일 이메일에 대해 인증 코드 발송 횟수
    @Value("${auth.send.window-seconds:300}")       private long windowSeconds;             // 발송 최대 횟수가 적용되는 시간
    @Value("${auth.verified-token.ttl-seconds:900}") private long verifiedTokenTtlSeconds;  // 토큰 유효시간 : 900초
    @Value("${service.display-name:알파노 컴퍼니}")  private String serviceName;              // 메일에 노출할 서비스명

    /**
     * 이메일로 인증코드 발송
     * @param email
     */
    @Transactional
    public void sendCodeForSignUp(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw EmailAlreadyExistsException.EXCEPTION;
        }

        long count = redis.incrementWithTtl(KEY_THROTTLE_SEND + email, Duration.ofSeconds(windowSeconds));
        if (count > maxPerWindow) {
            throw TooManyEmailRequestsException.EXCEPTION;
        }

        String code = generateCode(codeLen);
        redis.set(KEY_CODE + email, code, Duration.ofSeconds(ttlSeconds));

        String subject = MailTemplates.verificationSubject(serviceName);
        String html = MailTemplates.verificationHtml(code, serviceName);
        String text = MailTemplates.verificationText(code, serviceName);
        mailService.sendMail(email, subject, text, html);
    }

    /**
     * 코드 일치 시 일회용 토큰 발급 후 반환
     * @param email
     * @param inputCode
     * @return
     */
    @Transactional
    public VerifiedToken verifyCode(String email, String inputCode) {
        String key   = KEY_CODE + email;
        String saved = redis.get(key);
        if (saved != null && saved.equals(inputCode)) {
            redis.delete(key);

            String token = generateOpaqueToken(32);
            redis.set(KEY_VERIFIED_TOKEN + token, email, Duration.ofSeconds(verifiedTokenTtlSeconds));

            return new VerifiedToken(token, verifiedTokenTtlSeconds);
        }
        throw InvalidOrExpiredException.EXCEPTION;
    }

    @Transactional
    public String consumeVerifiedToken(String token) {
        String key = KEY_VERIFIED_TOKEN + token;
        String email = redis.get(key);
        if (email != null) {
            redis.delete(key); // 일회성 사용
            return email;
        }
        throw InvalidOrExpiredException.EXCEPTION;
    }

    private String generateCode(int len) {
        StringBuilder stringBuilder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        return stringBuilder.toString();
    }

    private String generateOpaqueToken(int numBytes) {
        byte[] buf = new byte[numBytes];
        secureRandom.nextBytes(buf);
        return base64Encoder.encodeToString(buf);
    }

    public record VerifiedToken(String value, long ttlSeconds) {}
}
