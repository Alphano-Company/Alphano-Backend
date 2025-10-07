package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.domain.match.dto.MatchJobMessage;
import com.alphano.alphano.domain.match.dto.MatchSqsMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqsMessageConsumer {

    SqsClient sqsClient = SqsClient.create();
    private final ObjectMapper objectMapper;
    private final MatchResultProcessor matchResultProcessor;

    @Value("${spring.cloud.aws.sqs.queues.backend}")
    private String matchResultQueueUrl;

    @Scheduled(fixedDelay = 5000)   // 5초마다 polling
    public void receiveMessages() {
        try {
            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(matchResultQueueUrl)
                    .maxNumberOfMessages(1)
                    .waitTimeSeconds(10)
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

            for (Message message : messages) {
                 if (handleMessage(message)){
                     deleteMessage(message);
                 }
            }
        } catch (SqsException e) {
            log.error("SQS 메시지 수신 실패", e);
        }
    }

    /**
     * 메시지 처리 로직
     * @param message
     */
    private boolean handleMessage(Message message) {
        try {
            String body = message.body();
            MatchSqsMessage sqsMessage = objectMapper.readValue(body, MatchSqsMessage.class);
            matchResultProcessor.process(sqsMessage);
            return true;
        } catch (Exception e) {
            log.error("SQS 메시지 처리 중 예외 발생", e);
            return false;
        }
    }

    /**
     * 처리 완료된 메시지 삭제
     * @param message
     */
    private void deleteMessage(Message message) {
        try {
            DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                    .queueUrl(matchResultQueueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();

            sqsClient.deleteMessage(deleteRequest);
            log.info("SQS 메시지 삭제 완료. messageId={}", message.messageId());
        } catch (SqsException e) {
            log.error("SQS 메시지 삭제 실패", e);
        }
    }
}
