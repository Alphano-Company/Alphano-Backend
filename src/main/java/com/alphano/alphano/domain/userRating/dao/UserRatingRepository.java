package com.alphano.alphano.domain.userRating.dao;

import com.alphano.alphano.domain.userRating.domain.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

}
