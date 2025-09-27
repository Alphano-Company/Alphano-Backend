package com.alphano.alphano.domain.submission.dao;

import com.alphano.alphano.domain.submission.domain.Submission;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    // 임시로 상대방 선택
    Optional<Submission> findFirstByProblemIdAndUserIdNotOrderByIdDesc(
            Long problemId, Long excludeUserId);
}
