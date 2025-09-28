package com.alphano.alphano.domain.submission.dao;

import com.alphano.alphano.domain.submission.domain.Submission;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    // 임시로 상대방 선택
    Optional<Submission> findFirstByProblemIdAndUserIdNotOrderByIdDesc(
            Long problemId, Long excludeUserId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
update Submission s
set s.isDefault = case when s.id = :targetId then true else false end
where s.user.id = :userId and s.problem.id = :problemId and (s.isDefault = true or s.id = :targetId)
""")
    int switchDefault(@Param("userId") Long userId,
                      @Param("problemId") Long problemId,
                      @Param("targetId") Long targetId);

    @Query("select s.problem.id from Submission s where s.id = :id and s.user.id = :userId")
    Optional<Long> findProblemIdByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    boolean existsByProblemIdAndUserId(Long problemId, Long userId);
}
