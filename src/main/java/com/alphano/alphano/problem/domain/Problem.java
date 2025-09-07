package com.alphano.alphano.problem.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.match.domain.Match;
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
public class Problem extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;

    private String content;
    private String inputFormat;
    private String outputFormat;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<ProblemImage> problemImages = new ArrayList<>();

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<UserRating> userRatings = new ArrayList<>();

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<UserHistory> userHistories = new ArrayList<>();
}
