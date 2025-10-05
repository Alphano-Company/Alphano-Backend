package com.alphano.alphano.domain.submission.domain;

import com.alphano.alphano.common.domain.BaseTimeEntity;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status = SubmissionStatus.UPLOADING;
    private String language;
    private boolean isDefault;
    private Integer win = 0;
    private Integer lose = 0;
    private Integer draw = 0;
    private String codeKey;
    private Integer codeLength;

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

    @Builder
    public Submission(String language, boolean isDefault,
                      String codeKey, Integer codeLength) {
        this.language = language;
        this.isDefault = isDefault;
        this.codeKey = codeKey;
        this.codeLength = codeLength;
    }

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
