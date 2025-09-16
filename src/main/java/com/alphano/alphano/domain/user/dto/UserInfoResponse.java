package com.alphano.alphano.domain.user.dto;

import com.alphano.alphano.domain.user.domain.User;

public record UserInfoResponse(
        Long userId,
        String nickname,
        String identifier
) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getNickname(),
                user.getIdentifier()
        );
    }
}
