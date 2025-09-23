package com.alphano.alphano.domain.submission.application;

import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.submission.dao.SubmissionQueryRepository;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.dto.response.SubmissionSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionService {
    private final ProblemRepository problemRepository;
    private final SubmissionQueryRepository submissionQueryRepository;

    public Page<SubmissionSummaryResponse> getAllSubmissions(Long userId, Long problemId, Pageable pageable) {
        if (!problemRepository.existsById(problemId)) {
            throw ProblemNotFoundException.EXCEPTION;
        }

        return submissionQueryRepository.getSubmissionsSummary(userId, problemId, pageable);
    }
}
