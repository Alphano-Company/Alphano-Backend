package com.alphano.alphano.domain.userRating.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRating extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_rating_id")
    private Long id;

    private double rating = 1500;
    private Integer win;
    private Integer lose;
    private Integer draw;
    private Integer winStreak;
    private Integer bestWinStreak;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserRating(Double rating, Integer win, Integer lose, Integer draw,
                      Integer winStreak, Integer bestWinStreak,
                      Problem problem, User user) {
        this.rating = (rating != null) ? rating : 1500.0;
        this.win = (win != null) ? win : 0;
        this.lose = (lose != null) ? lose : 0;
        this.draw = (draw != null) ? draw : 0;
        this.winStreak = (winStreak != null) ? winStreak : 0;
        this.bestWinStreak = (bestWinStreak != null) ? bestWinStreak : 0;
        this.problem = problem;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
