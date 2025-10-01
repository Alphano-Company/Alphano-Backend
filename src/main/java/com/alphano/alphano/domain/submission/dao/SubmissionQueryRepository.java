package com.alphano.alphano.domain.submission.dao;

import com.alphano.alphano.domain.submission.domain.QSubmission;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.domain.SubmissionStatus;
import com.alphano.alphano.domain.submission.dto.response.SubmissionSummaryResponse;
import com.alphano.alphano.domain.userRating.domain.QUserRating;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
                        submission.user.id.eq(userId),
                        submission.status.eq(SubmissionStatus.READY)
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

    public Double findMinDistance(Long problemId, Long userId, double x) {
        QSubmission submission = QSubmission.submission;
        QUserRating userRating = QUserRating.userRating;

        NumberExpression<Double> dist = Expressions.numberTemplate(Double.class, "abs({0} - {1})", userRating.rating, x);
        return queryFactory
                .select(dist.min())
                .from(submission)
                .join(userRating).on(
                        userRating.user.eq(submission.user),
                        userRating.problem.id.eq(problemId)
                )
                .where(
                        submission.problem.id.eq(problemId),
                        submission.user.id.ne(userId)   // 요청자 제외
                )
                .fetchOne();
    }

    public List<Submission> findAllWithExactDistance(Long problemId, Long userId, double x, double minDist) {
        QSubmission submission = QSubmission.submission;
        QUserRating userRating = QUserRating.userRating;

        NumberExpression<Double> dist =
                Expressions.numberTemplate(Double.class, "abs({0} - {1})", userRating.rating, x);

        return queryFactory
                .selectFrom(submission)
                .join(userRating).on(
                        userRating.user.eq(submission.user),
                        userRating.problem.id.eq(problemId)
                )
                .where(
                        submission.problem.id.eq(problemId),
                        submission.user.id.ne(userId),
                        dist.eq(minDist) // 동점 전원 조회
                )
                .fetch();
    }
}
