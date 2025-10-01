package com.alphano.alphano.domain.userRating.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.user.domain.User;
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

    private int rating = 1500;
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
}
