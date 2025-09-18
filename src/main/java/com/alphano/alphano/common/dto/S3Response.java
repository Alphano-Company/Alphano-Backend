package com.alphano.alphano.common.dto;

public record S3Response(
        String key,
        String url
) {
    public static S3Response of(String key, String url) {
        return new S3Response(key, url);
    }
}
