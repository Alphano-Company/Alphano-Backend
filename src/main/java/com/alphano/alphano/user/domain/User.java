package com.alphano.alphano.user.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.submission.domain.Submission;
import com.alphano.alphano.userRating.domain.UserRating;
import com.alphano.alphano.userRatingHistory.domain.UserHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String profileImageUrl;
    private String identifier;
    private String password;
    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRating> userRatings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserHistory> userHistories = new ArrayList<>();

}
