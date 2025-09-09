package com.alphano.alphano.common.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@RequiredArgsConstructor
public class SnsConfig {
    @Value("${spring.cloud.aws.region.static}")
    private String awsRegion;

    private SnsClient snsClient;

    @Bean
    public SnsClient snsClient() {
        this.snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return this.snsClient;
    }

    @PreDestroy
    public void shutdown() {
        if (snsClient != null) snsClient.close();
    }
}
