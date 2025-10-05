package com.alphano.alphano.domain.problem.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.userRating.domain.UserRating;
import com.alphano.alphano.domain.userRatingHistory.domain.UserHistory;
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

    private String title;
    private String content;
    private String inputFormat;
    private String outputFormat;
    private int submissionCount;
    private int submitterCount;
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_image_id")
    private ProblemImage iconKey;

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

    public void addSubmission(Submission submission) {
        this.submissions.add(submission);
        submission.setProblem(this);
    }

    public void addUserRating(UserRating userRating) {
        this.userRatings.add(userRating);
        userRating.setProblem(this);
    }
}
