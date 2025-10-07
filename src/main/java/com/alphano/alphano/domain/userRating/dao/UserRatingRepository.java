package com.alphano.alphano.domain.userRating.dao;

import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.userRating.domain.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    List<UserRating> problem(Problem problem);
}
