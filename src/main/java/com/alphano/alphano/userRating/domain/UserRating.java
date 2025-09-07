package com.alphano.alphano.userRating.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.problem.domain.Problem;
import com.alphano.alphano.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRating extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_rating_id")
    private Long id;

    private Integer rating;
    private Integer win;
    private Integer lose;
    private Integer draw;
    private Integer winStreak;
    private Integer BestWinStreak;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
