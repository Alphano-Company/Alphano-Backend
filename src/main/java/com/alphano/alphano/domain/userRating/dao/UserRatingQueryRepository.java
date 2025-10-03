package com.alphano.alphano.domain.userRating.dao;

import com.alphano.alphano.domain.problem.dto.query.LeaderboardQuery;
import com.alphano.alphano.domain.user.domain.QUser;
import com.alphano.alphano.domain.userRating.domain.QUserRating;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRatingQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<LeaderboardQuery> getLeaderboard(Long problemId, Pageable pageable) {
        QUser user = QUser.user;
        QUserRating userRating = QUserRating.userRating;

        JPAQuery<LeaderboardQuery> query = queryFactory
                .select(Projections.constructor(LeaderboardQuery.class,
                        user.id,
                        user.identifier,
                        user.profileImageKey,
                        userRating.rating,
                        userRating.win,
                        userRating.lose,
                        userRating.draw,
                        userRating.winStreak,
                        userRating.bestWinStreak
                ))
                .from(userRating)
                .join(userRating.user, user)
                .where(userRating.problem.id.eq(problemId))
                .orderBy(
                        userRating.rating.desc(),
                        userRating.win.desc(),
                        userRating.updatedAt.asc()
                );

        List<LeaderboardQuery> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(userRating.count())
                .from(userRating)
                .where(userRating.problem.id.eq(problemId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Double findCurrentRating(Long problemId, Long userId) {
        QUserRating userRating = QUserRating.userRating;

        return queryFactory
                .select(userRating.rating)
                .from(userRating)
                .where(
                        userRating.problem.id.eq(problemId),
                        userRating.user.id.eq(userId)
                )
                .fetchOne();
    }

    public Map<Long, Double> findCurrentRatings(Long problemId, List<Long> candidateIds) {
        QUserRating userRating = QUserRating.userRating;

        List<Tuple> results = queryFactory
                .select(userRating.user.id, userRating.rating)
                .from(userRating)
                .where(
                        userRating.problem.id.eq(problemId),
                        userRating.user.id.in(candidateIds)
                )
                .fetch();

        // 결과를 Map<userId, rating>으로 변환
        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(userRating.user.id),
                        tuple -> tuple.get(userRating.rating)
                ));
    }
}
