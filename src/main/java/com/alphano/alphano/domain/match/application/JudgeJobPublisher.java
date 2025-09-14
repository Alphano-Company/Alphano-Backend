package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.domain.match.dto.MatchJobMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgeJobPublisher {

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.aws.sns.topic.match-request}")
    private String matchRequestArn;

    @Value("${spring.cloud.aws.sns.topic.match-result}")
    private String matchResultArn;

    public void publishMatchRequest(MatchJobMessage message) {
        publishJson(matchRequestArn, message);
    }

    public void publishMatchResult(Object resultPayload) {
        publishJson(matchResultArn, resultPayload);
    }

    private void publishJson(String topicArn, Object payload) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            PublishRequest req = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(body)
                    .build();
            snsClient.publish(req);
            log.info("SNS published. topic={} bytes={}", topicArn, body.length());
        } catch (SnsException e) {
            log.error("SNS publish failed. topic={} status={}", topicArn, e.statusCode(), e);
            throw e;
        } catch (Exception e) {
            log.error("SNS publish failed. topic={}", topicArn, e);
            throw new RuntimeException("SNS publish failed", e);
        }
    }
}
