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
        Outcome agent1 = toOutcome(result, agent1UserId, agent1UserId, agent2UserId);
        Outcome agent2 = toOutcome(result, agent2UserId, agent1UserId, agent2UserId);

        RatingResult rating1 = updateOne(problemId, agent1UserId, agent1);
        RatingResult rating2 = updateOne(problemId, agent2UserId, agent2);

        // userRating과 같은 problemId를 갖는 submisison들의 승/무/패를 더한다.
        // 연승, 최고 연승을 업뎅이트한다.
        // elo 레이팅 방식으로 레이팅을 업데이트한다.
        // 레이팅 변화량을 통해 userHistory를 생성한다.
    }

    private RatingResult updateOne(Long problemId, Long userId, Outcome outcome) {
        UserRating r = userRatingRepository.findByProblemIdAndUserIdForUpdate(problemId, userId)
                .orElseGet(() -> createNew(problemId, userId));
    }

    protected Outcome toOutcome(MatchResult result, Long who, Long agent1, Long agent2) {
        return switch (result) {
            case DRAW       -> Outcome.DRAW;
            case AGENT1_WIN -> who.equals(agent1) ? Outcome.WIN : Outcome.LOSE;
            case AGENT2_WIN -> who.equals(agent2) ? Outcome.WIN : Outcome.LOSE;
        };
    }

    public enum Outcome { WIN, DRAW, LOSE }

    public record RatingResult(double before, double after, double delta) {}

}
