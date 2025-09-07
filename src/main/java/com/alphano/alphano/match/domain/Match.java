package com.alphano.alphano.match.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.problem.domain.Problem;
import com.alphano.alphano.submission.domain.Submission;
import com.alphano.alphano.userRatingHistory.domain.Outcome;
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
public class Match  extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    private String log;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Enumerated(EnumType.STRING)
    private MatchResult result;

    @Enumerated(EnumType.STRING)
    private EndReason agent1EndReason;

    @Enumerated(EnumType.STRING)
    private EndReason agent2EndReason;

    private Integer randomSeed;

    private Long agent1Id;

    private Long agent2Id;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<UserHistory> userHistories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent1_submit_id")
    private Submission agent1Submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent2_submit_id")
    private Submission agent2Submission;
}
