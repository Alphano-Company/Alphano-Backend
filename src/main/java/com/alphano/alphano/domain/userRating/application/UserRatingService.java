package com.alphano.alphano.domain.userRating.application;

import com.alphano.alphano.domain.match.domain.MatchResult;
import com.alphano.alphano.domain.userRating.dao.UserRatingRepository;
import com.alphano.alphano.domain.userRating.domain.UserRating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRatingService {


    private final UserRatingRepository userRatingRepository;

    @Transactional()
    public void applyResult(Long problemId, MatchResult result, Long agent1UserId, Long agent2UserId) {
    }

}
