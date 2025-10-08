package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.domain.match.dto.MatchSqsMessage;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class SqsMessageConsumer {

    private final MatchResultProcessor matchResultProcessor;

    @SqsListener(value = "${spring.cloud.aws.sqs.queues.backend}")
    public void listen(MatchSqsMessage message) {
        log.info("SQS로부터 메시지를 수신했습니다. MatchId: {}", message.matchId());
        try {
            matchResultProcessor.process(message);
            log.info("메시지 처리가 성공적으로 완료되었습니다. MatchId: {}", message.matchId());
        } catch (Exception e) {
            log.error("SQS 메시지 처리 중 예외 발생. MatchId: {}", message.matchId(), e);
            throw new RuntimeException("메시지 처리 실패", e);
        }
    }
}
