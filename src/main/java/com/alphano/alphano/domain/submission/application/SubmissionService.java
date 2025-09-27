package com.alphano.alphano.domain.submission.application;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.submission.dao.SubmissionQueryRepository;
import com.alphano.alphano.domain.submission.dao.SubmissionRepository;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.dto.response.SubmissionDetailResponse;
import com.alphano.alphano.domain.submission.dto.response.SubmissionSummaryResponse;
import com.alphano.alphano.domain.submission.exception.SubmissionForbiddenException;
import com.alphano.alphano.domain.submission.exception.SubmissionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionService {
    private final ProblemRepository problemRepository;
    private final SubmissionQueryRepository submissionQueryRepository;
    private final S3Service s3Service;
    private final SubmissionRepository submissionRepository;

    public Page<SubmissionSummaryResponse> getAllSubmissions(Long userId, Long problemId, Pageable pageable) {
        if (!problemRepository.existsById(problemId)) {
            throw ProblemNotFoundException.EXCEPTION;
        }

        return submissionQueryRepository.getSubmissionsSummary(userId, problemId, pageable);
    }

    public SubmissionDetailResponse getSubmissionDetail(Long userId, Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);

        if (!userId.equals(submission.getUser().getId())) {
            throw SubmissionForbiddenException.EXCEPTION;
        }

        return SubmissionDetailResponse.from(submission, s3Service);
    }

    @Transactional
    public void setDefaultSubmission(Long userId, Long submissionId) {
        Long problemId = submissionRepository.findProblemIdByIdAndUserId(submissionId, userId)
                .orElseThrow(() -> SubmissionForbiddenException.EXCEPTION);

        int updateRow = submissionRepository.switchDefault(userId, problemId, submissionId);
        if (updateRow == 0) {
            throw new IllegalStateException("기본 제출 변경 실패");
        }
    }
}
