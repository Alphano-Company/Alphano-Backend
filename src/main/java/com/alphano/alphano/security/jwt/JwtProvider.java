package com.alphano.alphano.security.jwt;

import com.alphano.alphano.domain.user.dao.UserRepository;
import com.alphano.alphano.security.exception.AccessExpiredException;
import com.alphano.alphano.security.exception.InvalidSignatureException;
import com.alphano.alphano.security.exception.InvalidTokenException;
import com.alphano.alphano.security.exception.RefreshExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.access}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.expiration.refresh}")
    private long REFRESH_TOKEN_EXPIRATION;

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String identifier, List<String> roles) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("identifier", identifier)
                .claim("roles", roles)
                .claim("category", "access")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("category", "refresh")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return jws.getPayload();
        } catch (ExpiredJwtException e){
            Claims claims = e.getClaims();
            String category = claims != null ? claims.get("category", String.class) : null;
            if ("refresh".equals(category)) {
               throw RefreshExpiredException.EXCEPTION;
            } else {
                throw AccessExpiredException.EXCEPTION;
            }
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e) {
            throw InvalidTokenException.EXCEPTION;
        } catch (SecurityException e) {
            throw InvalidSignatureException.EXCEPTION;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e){
            return false;
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e) {
            throw InvalidTokenException.EXCEPTION;
        } catch (SecurityException e) {
            throw InvalidSignatureException.EXCEPTION;
        }
    }
}
