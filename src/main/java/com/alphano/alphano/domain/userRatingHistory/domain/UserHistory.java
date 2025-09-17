package com.alphano.alphano.domain.userRatingHistory.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_history_id")
    private Long id;

    private Integer ratingBefore;
    private Integer ratingAfter;
    private Integer ratingDelta;

    private Integer win;
    private Integer lose;
    private Integer draw;

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
}
