package com.alphano.alphano.match.application;

import com.alphano.alphano.match.domain.MatchStatus;
import com.alphano.alphano.match.dto.MatchJobMessage;
import com.alphano.alphano.match.dto.request.MatchRequest;
import com.alphano.alphano.match.dto.response.MatchResponse;
import com.alphano.alphano.submission.dao.SubmissionRepository;
import com.alphano.alphano.submission.domain.Submission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final SubmissionRepository submissionRepository;
    private final JudgeJobPublisher judgeJobPublisher;

    public MatchResponse create(Long problemId, @Valid MatchRequest request) {
        Submission mine = submissionRepository
                .findByIdAndUserId(request.submissionId(), request.agent1Id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid submission"));
        if (!Objects.equals(mine.getProblem().getId(), problemId)) {
            throw new IllegalArgumentException("Invalid submission");
        }

        // 상대 선택
        Submission opp = submissionRepository
                .findFirstByProblemIdAndUserIdNotOrderByIdDesc(problemId, request.agent1Id())
                .orElseThrow(() -> new RuntimeException("No opponent found"));

        if (mine.getCodeKey() == null || opp.getCodeKey() == null)
            throw new IllegalStateException("code key missing");

        // 랜덤 시드 생성
        int seed = ThreadLocalRandom.current().nextInt();

        // SNS 발행
        MatchJobMessage msg = new MatchJobMessage(
                problemId,
                seed,
                new MatchJobMessage.Agent(mine.getUser().getId(), mine.getId(), mine.getCodeKey(), mine.getLanguage()),
                new MatchJobMessage.Agent(opp.getUser().getId(), opp.getId(), opp.getCodeKey(), opp.getLanguage())
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
