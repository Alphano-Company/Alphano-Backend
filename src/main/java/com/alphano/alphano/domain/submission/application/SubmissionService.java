package com.alphano.alphano.domain.submission.application;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.common.upload.KeyGenerator;
import com.alphano.alphano.common.upload.SubmissionKeyGenerator;
import com.alphano.alphano.domain.match.dao.MatchRepository;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.match.domain.MatchResult;
import com.alphano.alphano.domain.match.exception.MatchNotFoundException;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.submission.dao.SubmissionQueryRepository;
import com.alphano.alphano.domain.submission.dao.SubmissionRepository;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.domain.SubmissionStatus;
import com.alphano.alphano.domain.submission.dto.request.AddSubmissionRequest;
import com.alphano.alphano.domain.submission.dto.response.SubmissionDetailResponse;
import com.alphano.alphano.domain.submission.dto.response.SubmissionSummaryResponse;
import com.alphano.alphano.domain.submission.exception.SubmissionCodeObjectNotFoundException;
import com.alphano.alphano.domain.submission.exception.SubmissionForbiddenException;
import com.alphano.alphano.domain.submission.exception.SubmissionNotFoundException;
import com.alphano.alphano.domain.submission.exception.SubmissionNotReadyException;
import com.alphano.alphano.domain.user.dao.UserRepository;
import com.alphano.alphano.domain.user.domain.User;
import com.alphano.alphano.domain.userRating.dao.UserRatingRepository;
import com.alphano.alphano.domain.userRating.domain.UserRating;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.alphano.alphano.common.consts.AlphanoStatic.ALPHANO_SUBMISSIONS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionService {
    private final ProblemRepository problemRepository;
    private final SubmissionQueryRepository submissionQueryRepository;
    private final S3Service s3Service;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final UserRatingRepository userRatingRepository;
    private final MatchRepository matchRepository;

    public Page<SubmissionSummaryResponse> getAllSubmissions(Long userId, Long problemId, Pageable pageable) {
        if (!problemRepository.existsById(problemId)) {
            throw ProblemNotFoundException.EXCEPTION;
        }

        return submissionQueryRepository.getSubmissionsSummary(userId, problemId, pageable);
    }

    public SubmissionDetailResponse getSubmissionDetail(Long userId, Long submissionId) {
        Submission submission = submissionRepository.findByIdAndUserId(submissionId, userId)
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);

        if (!submission.isReady()) throw SubmissionNotReadyException.EXCEPTION;

        return SubmissionDetailResponse.from(submission, s3Service);
    }

    @Transactional
    public void setDefaultSubmission(Long userId, Long submissionId) {
        Long problemId = submissionRepository.findProblemIdByIdAndUserId(submissionId, userId)
                .orElseThrow(() -> SubmissionForbiddenException.EXCEPTION);

        int updateRow = submissionRepository.setDefault(userId, problemId, submissionId, SubmissionStatus.READY);
        if (updateRow == 0) {
            throw SubmissionNotReadyException.EXCEPTION;
        }
        submissionRepository.unsetDefault(userId, problemId, submissionId);
    }

    @Transactional
    public S3Response addSubmission(Long problemId, Long userId, AddSubmissionRequest request) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> ProblemNotFoundException.EXCEPTION);
        User user = userRepository.getReferenceById(userId);

        boolean submissionExists = submissionRepository.existsByProblemIdAndUserId(problemId, userId);

        Submission submission = Submission.builder()
                .language(request.language())
                .isDefault(!submissionExists)
                .codeLength(request.codeLength())
                .build();
        user.addSubmission(submission);
        problem.addSubmission(submission);
        submissionRepository.save(submission);

        if (!submissionExists) {
            UserRating userRating = UserRating.create();
            user.addUserRating(userRating);
            problem.addUserRating(userRating);

            userRatingRepository.save(userRating);
        }

        KeyGenerator keyGenerator = new SubmissionKeyGenerator(problemId, userId, submission.getId());
        String codeKey = keyGenerator.generateKey(request.fileName());
        submission.updateCodeKey(codeKey);

        return s3Service.createPresignedPutUrl(ALPHANO_SUBMISSIONS, request.fileName(), request.metadata(), keyGenerator);
    }

    @Transactional
    public String finalizeSubmission(Long submissionId, Long userId) {
        Submission submission = submissionRepository.findByIdAndUserId(submissionId, userId)
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);
        if (submission.isReady()) return "이미 확인된 객체입니다.";
        if (s3Service.objectExists(ALPHANO_SUBMISSIONS, submission.getCodeKey())) {
            submission.setReady();
            return "객체가 확인되었습니다.";
        } else {
            throw SubmissionCodeObjectNotFoundException.EXCEPTION;
        }
    }

    @Transactional
    public void applyResult(Long matchId, MatchResult result) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> MatchNotFoundException.EXCEPTION);

        Submission agent1 = submissionRepository.findByIdForUpdate(match.getAgent1Submission().getId())
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);
        Submission agent2 = submissionRepository.findByIdForUpdate(match.getAgent2Submission().getId())
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);

        switch (result) {
            case AGENT1_WIN -> {
                agent1.increaseWin();
                agent2.increaseLose();
            }
            case AGENT2_WIN -> {
                agent2.increaseWin();
                agent1.increaseLose();
            }
            case DRAW -> {
                agent1.increaseDraw();
                agent2.increaseDraw();
            }
        }

    }
}
