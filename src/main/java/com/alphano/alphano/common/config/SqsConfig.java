package com.alphano.alphano.common.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@RequiredArgsConstructor
public class SqsConfig {
    @Value("${spring.cloud.aws.region.static}")
    private String awsRegion;

    private SqsAsyncClient sqsAsyncClient;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        this.sqsAsyncClient = SqsAsyncClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return this.sqsAsyncClient;
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

    @PreDestroy
    public void shutdown() {
        if (sqsAsyncClient != null) sqsAsyncClient.close();
    }
}
