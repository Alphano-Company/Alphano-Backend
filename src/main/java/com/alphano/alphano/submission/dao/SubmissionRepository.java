package com.alphano.alphano.submission.dao;

import com.alphano.alphano.submission.domain.Submission;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {
    Optional<Submission> findByIdAndUserId(Long id, Long userId);

    // 임시로 상대방 선택
    Optional<Submission> findFirstByProblemIdAndUserIdNotOrderByIdDesc(
            Long problemId, Long excludeUserId);
}
