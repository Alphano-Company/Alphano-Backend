package com.alphano.alphano.domain.userRating.dao;

import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.userRating.domain.UserRating;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    List<UserRating> problem(Problem problem);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ur FROM UserRating ur WHERE ur.problem.id = :problemId AND ur.user.id = :userId")
    Optional<UserRating> findByProblemIdAndUserIdForUpdate(@Param("problemId") Long problemId, @Param("userId") Long userId);
}
