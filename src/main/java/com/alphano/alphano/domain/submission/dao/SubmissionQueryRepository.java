package com.alphano.alphano.domain.submission.dao;

import com.alphano.alphano.domain.submission.domain.QSubmission;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.dto.response.SubmissionSummaryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubmissionQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<SubmissionSummaryResponse> getSubmissionsSummary(Long userId, Long problemId, Pageable pageable) {
        QSubmission submission = QSubmission.submission;

        JPAQuery<SubmissionSummaryResponse> query = queryFactory
                .select(Projections.constructor(SubmissionSummaryResponse.class,
                        submission.id,
                        submission.win,
                        submission.lose,
                        submission.draw,
                        submission.language,
                        submission.codeLength,
                        submission.createdAt,
                        submission.isDefault
                ))
                .from(submission)
                .where(
                        submission.problem.id.eq(problemId),
                        submission.user.id.eq(userId)
                )
                .orderBy(
                        submission.createdAt.desc(),
                        submission.id.desc()
                );

        List<SubmissionSummaryResponse> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(submission.count())
                .from(submission)
                .where(
                        submission.problem.id.eq(problemId),
                        submission.user.id.eq(userId)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Optional<Submission> getDefaultSubmission(Long problemId, Long userId) {
        QSubmission submission = QSubmission.submission;
        return Optional.ofNullable(
                queryFactory.selectFrom(submission)
                        .where(
                                submission.problem.id.eq(problemId),
                                submission.user.id.eq(userId),
                                submission.isDefault.isTrue()
                        )
                        .orderBy(submission.id.desc())
                        .fetchFirst()
        );
    }
}
