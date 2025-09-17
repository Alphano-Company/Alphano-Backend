package com.alphano.alphano.domain.submission.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    private String language;
    private boolean isDefault;
    private Integer win;
    private Integer lose;
    private Integer draw;
    private String codeKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @OneToMany(mappedBy = "agent1Submission", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    @OneToMany(mappedBy = "agent2Submission", cascade = CascadeType.ALL)
    private List<Match> matchesAsAgent2 = new ArrayList<>();
}
