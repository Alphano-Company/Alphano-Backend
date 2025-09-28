package com.alphano.alphano.common.upload;

public class ProfileKeyGenerator implements KeyGenerator {
    private final Long userId;
    public ProfileKeyGenerator(Long userId) {
        this.userId = userId;
    }

    @Override
    public String generateKey(String fileName) {
        return "users/" + userId + "/profiles/" + fileName;
    }
}
