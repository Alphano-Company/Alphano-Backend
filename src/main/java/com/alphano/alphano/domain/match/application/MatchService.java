package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.domain.match.domain.MatchStatus;
import com.alphano.alphano.domain.match.dto.MatchJobMessage;
import com.alphano.alphano.domain.match.dto.request.MatchRequest;
import com.alphano.alphano.domain.match.dto.response.MatchResponse;
import com.alphano.alphano.domain.match.exception.OpponentNotFoundException;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.submission.dao.SubmissionRepository;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.exception.SubmissionCodeKeyMissingException;
import com.alphano.alphano.domain.submission.exception.SubmissionNotFoundException;
import com.alphano.alphano.domain.submission.exception.SubmissionProblemMismatchException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final SubmissionRepository submissionRepository;
    private final JudgeJobPublisher judgeJobPublisher;
    private final ProblemRepository problemRepository;

    public MatchResponse create(Long problemId, @Valid MatchRequest request, Long userId) {
        if (problemRepository.findById(problemId).isEmpty()) {
            throw ProblemNotFoundException.EXCEPTION;
        }

        Submission mine = submissionRepository
                .findByIdAndUserId(request.submissionId(), userId)
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);
        if (!Objects.equals(mine.getProblem().getId(), problemId)) {
            throw SubmissionProblemMismatchException.EXCEPTION;
        }

        // 상대 선택
        Submission opp = submissionRepository
                .findFirstByProblemIdAndUserIdNotOrderByIdDesc(problemId, userId)
                .orElseThrow(() -> OpponentNotFoundException.EXCEPTION);

        if (mine.getCodeKey() == null || opp.getCodeKey() == null)
            throw SubmissionCodeKeyMissingException.EXCEPTION;

        // 랜덤 시드 생성
        int seed = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

        // SNS 발행
        MatchJobMessage msg = new MatchJobMessage(
                problemId,
                seed,
                new MatchJobMessage.Agent(mine.getUser().getId(), mine.getId(), mine.getLanguage(), mine.getCodeKey()),
                new MatchJobMessage.Agent(opp.getUser().getId(), opp.getId(), opp.getLanguage(), opp.getCodeKey())
        );
        judgeJobPublisher.publishMatchRequest(msg);

        return new MatchResponse(
                problemId,
                opp.getUser().getId(),
                seed,
                MatchStatus.QUEUED
        );
    }
}
