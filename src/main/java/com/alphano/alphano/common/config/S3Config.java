package com.alphano.alphano.common.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class S3Config {

    @Value("${spring.cloud.aws.region.static}")
    private String awsRegion;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @Bean
    public S3Client amazonS3() {
        this.s3Client = S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return this.s3Client;
    }

    @Bean
    public S3Presigner amazonS3Presigner() {
        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return this.s3Presigner;
    }

    @PreDestroy
    public void shutdown() {
        try {
            if (s3Presigner != null) s3Presigner.close();
            if (s3Client != null) s3Client.close();
            log.info("S3 SDK 자원 정상 해제 완료");
        } catch (Exception e) {
            log.warn("S3 SDK 자원 해제 실패", e);
        }
    }
}