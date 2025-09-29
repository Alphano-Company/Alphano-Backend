package com.alphano.alphano.domain.submission.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Submission extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;
    private String language;
    private boolean isDefault;
    private Integer win;
    private Integer lose;
    private Integer draw;
    private String codeKey;
    private Integer codeLength;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @OneToMany(mappedBy = "agent1Submission", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Match> matches = new ArrayList<>();

    @OneToMany(mappedBy = "agent2Submission", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Match> matchesAsAgent2 = new ArrayList<>();

    public void updateCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setUploading() {
        this.status = SubmissionStatus.UPLOADING;
    }

    public void setReady() {
        this.status = SubmissionStatus.READY;
    }

    public boolean isReady() {
        return this.status == SubmissionStatus.READY;
    }
}
