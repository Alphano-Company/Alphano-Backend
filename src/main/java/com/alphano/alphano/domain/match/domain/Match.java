package com.alphano.alphano.domain.match.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.userRatingHistory.domain.UserHistory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Match  extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    private String logKey;

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
    @Builder.Default
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

    public static Match createQueuedMatch(Problem problem, Submission mine, Submission opp, int seed) {
        return Match.builder()
                .status(MatchStatus.QUEUED)
                .randomSeed(seed)
                .agent1Id(mine.getUser().getId())
                .agent2Id(opp.getUser().getId())
                .problem(problem)
                .agent1Submission(mine)
                .agent2Submission(opp)
                .build();
    }
}
