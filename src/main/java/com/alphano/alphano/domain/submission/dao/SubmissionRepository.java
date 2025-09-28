package com.alphano.alphano.domain.submission.dao;

import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.domain.SubmissionStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // 임시로 상대방 선택
    Optional<Submission> findFirstByProblemIdAndUserIdNotOrderByIdDesc(
            Long problemId, Long excludeUserId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
update Submission s
set s.isDefault = true
where s.id = :targetId and s.user.id = :userId and s.problem.id = :problemId and s.status = :status
""")
    int setDefault(@Param("userId") Long userId,
                   @Param("problemId") Long problemId,
                   @Param("targetId") Long targetId,
                   @Param("status") SubmissionStatus status);

    @Modifying
    @Query("""
update Submission s
set s.isDefault = false
where s.user.id = :userId and s.problem.id = :problemId and s.isDefault = true and s.id <> :targetId
""")
    int unsetDefault(@Param("userId") Long userId,
                    @Param("problemId") Long problemId,
                    @Param("targetId") Long targetId);

    @Query("select s.problem.id from Submission s where s.id = :id and s.user.id = :userId")
    Optional<Long> findProblemIdByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    boolean existsByProblemIdAndUserId(Long problemId, Long userId);

    @Query("""
select s from Submission s
join fetch s.user u
where s.id = :id and u.id = :userId
""")
    Optional<Submission> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
