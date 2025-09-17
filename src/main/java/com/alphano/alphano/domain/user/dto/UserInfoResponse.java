package com.alphano.alphano.domain.user.dto;

import com.alphano.alphano.domain.user.domain.User;

public record UserInfoResponse(
        Long userId,
        String nickname,
        String identifier,
        String accessToken,
        String refreshToken
) {
    public static UserInfoResponse from(User user, String accessToken, String refreshToken) {
        return new UserInfoResponse(
                user.getId(),
                user.getNickname(),
                user.getIdentifier(),
                accessToken,
                refreshToken
        );
    }
}
