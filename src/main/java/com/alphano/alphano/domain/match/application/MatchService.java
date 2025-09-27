package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.domain.match.dao.MatchRepository;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.match.domain.MatchStatus;
import com.alphano.alphano.domain.match.dto.MatchJobMessage;
import com.alphano.alphano.domain.match.dto.response.MatchResponse;
import com.alphano.alphano.domain.match.exception.OpponentNotFoundException;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.submission.dao.SubmissionQueryRepository;
import com.alphano.alphano.domain.submission.dao.SubmissionRepository;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.exception.SubmissionCodeKeyMissingException;
import com.alphano.alphano.domain.submission.exception.SubmissionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final SubmissionRepository submissionRepository;
    private final JudgeJobPublisher judgeJobPublisher;
    private final ProblemRepository problemRepository;
    private final SubmissionQueryRepository submissionQueryRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public MatchResponse create(Long problemId, Long userId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> ProblemNotFoundException.EXCEPTION);

        Submission mine = submissionQueryRepository.getDefaultSubmission(problemId, userId)
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);

        // 상대 선택 : 수정 예정
        Submission opp = submissionRepository
                .findFirstByProblemIdAndUserIdNotOrderByIdDesc(problemId, userId)
                .orElseThrow(() -> OpponentNotFoundException.EXCEPTION);

        if (mine.getCodeKey() == null || opp.getCodeKey() == null)
            throw SubmissionCodeKeyMissingException.EXCEPTION;

        // 랜덤 시드 생성
        int seed = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

        Match match = Match.createQueuedMatch(problem, mine, opp, seed);
        Match saved = matchRepository.save(match);

        // SNS 발행
        MatchJobMessage msg = new MatchJobMessage(
                problemId,
                saved.getId(),
                seed,
                new MatchJobMessage.Agent(mine.getUser().getId(), mine.getId(), mine.getLanguage(), mine.getCodeKey()),
                new MatchJobMessage.Agent(opp.getUser().getId(), opp.getId(), opp.getLanguage(), opp.getCodeKey())
        );
        judgeJobPublisher.publishMatchRequest(msg);

        return MatchResponse.of(problemId, saved.getId(), opp.getUser().getId(), seed, MatchStatus.QUEUED);
    }
}
