package com.alphano.alphano.domain.userRating.dao;

import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.userRating.domain.UserRating;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    List<UserRating> problem(Problem problem);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserRating> findByProblemIdAndUserIdForUpdate(Long problemId, Long userId);
}
