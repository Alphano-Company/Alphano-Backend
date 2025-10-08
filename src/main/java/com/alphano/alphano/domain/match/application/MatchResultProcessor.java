package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.domain.match.dao.MatchRepository;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.match.domain.MatchStatus;
import com.alphano.alphano.domain.match.dto.MatchSqsMessage;
import com.alphano.alphano.domain.match.exception.MatchNotFoundException;
import com.alphano.alphano.domain.submission.application.SubmissionService;
import com.alphano.alphano.domain.userHistory.dao.UserHistoryRepository;
import com.alphano.alphano.domain.userRating.application.UserRatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchResultProcessor {

    private final UserHistoryRepository userHistoryRepository;
    private final MatchRepository matchRepository;
    private final SubmissionService submissionService;
    private final UserRatingService userRatingService;

    @Transactional
    public void process(MatchSqsMessage sqsMessage) {
        if (userHistoryRepository.existsByMatchId(sqsMessage.matchId())){
            return;
        }

        Match match = matchRepository.findById(sqsMessage.matchId())
                .orElseThrow(() -> MatchNotFoundException.EXCEPTION);

        match.update(sqsMessage);

        if (match.getStatus() == MatchStatus.FAILED) {
            log.warn("매치 ID {}의 채점이 FAILED 상태로 처리되었습니다. 레이팅/전적 반영 없이 종료합니다.", match.getId());
            return;
        }

        submissionService.applyResult(
                sqsMessage.matchId(),
                sqsMessage.result()
        );

        userRatingService.applyResult(
                sqsMessage.matchId(),
                sqsMessage.problemId(),
                sqsMessage.result(),
                sqsMessage.agent1UserId(),
                sqsMessage.agent1UserId());
    }
}
