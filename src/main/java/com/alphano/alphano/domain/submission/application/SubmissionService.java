package com.alphano.alphano.domain.submission.application;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.common.upload.KeyGenerator;
import com.alphano.alphano.common.upload.SubmissionKeyGenerator;
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

        boolean exists = submissionRepository.existsByProblemIdAndUserId(problemId, userId);

        Submission submission = Submission.builder()
                .language(request.language())
                .isDefault(!exists)
                .codeLength(request.codeLength())
                .build();
        submission.setUploading();
        user.addSubmission(submission);
        problem.addSubmission(submission);
        submissionRepository.save(submission);

        KeyGenerator keyGenerator = new SubmissionKeyGenerator(problemId, userId, submission.getId());
        String codeKey = keyGenerator.generateKey(request.fileName());
        submission.updateCodeKey(codeKey);

        return s3Service.createPresignedPutUrl(ALPHANO_SUBMISSIONS, request.fileName(), request.metadata(), keyGenerator);
    }
}
