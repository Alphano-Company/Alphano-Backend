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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            // 채점에 실패했을 경우 -> 레이팅, 승/무/패 반영은 어떻게?
            // throw 채점에 실패했습니다.
        }

        submissionService.applyResult(
                sqsMessage.matchId(),
                sqsMessage.result()
        );

        userRatingService.applyResult(
                sqsMessage.problemId(),
                sqsMessage.result(),
                sqsMessage.agent1UserId(),
                sqsMessage.agent1UserId());
    }
}
