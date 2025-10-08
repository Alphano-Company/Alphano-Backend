package com.alphano.alphano.domain.userHistory.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.common.util.RatingUtils;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.user.domain.User;
import com.alphano.alphano.domain.userRating.application.UserRatingService;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserHistory extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_history_id")
    private Long id;

    // Applying the sigmoid function
    private Double ratingBefore;
    private Double ratingAfter;
    private Double ratingDelta;

    @Enumerated(EnumType.STRING)
    private Outcome outcome;
    private Long opponentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    public static UserHistory create(Match match, User user, Problem problem, double before, double after, Long opponentId, Outcome outcome) {
        double alphanoBefore = RatingUtils.convert(before);
        double alphanoAfter = RatingUtils.convert(after);

        UserHistory history =  UserHistory.builder()
                .match(match)
                .user(user)
                .problem(problem)
                .ratingBefore(alphanoBefore)
                .ratingAfter(alphanoAfter)
                .ratingDelta(alphanoAfter - alphanoBefore)
                .opponentId(opponentId)
                .outcome(outcome)
                .build();
        match.addUserHistory(history);
        return history;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
