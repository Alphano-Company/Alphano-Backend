package com.alphano.alphano.domain.problem.dao;

import com.alphano.alphano.domain.problem.domain.QProblem;
import com.alphano.alphano.domain.problem.domain.QProblemImage;
import com.alphano.alphano.domain.problem.dto.query.HomeProblemQuery;
import com.alphano.alphano.domain.problem.dto.query.ProblemSummaryQuery;
import com.alphano.alphano.domain.userRating.domain.QUserRating;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.alphano.alphano.domain.problem.domain.QProblem.problem;
import static com.alphano.alphano.domain.problem.domain.QProblemImage.problemImage;

@Repository
@RequiredArgsConstructor
public class ProblemQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<ProblemSummaryQuery> findAllSummary(Pageable pageable) {
        JPAQuery<ProblemSummaryQuery> query = queryFactory
                .select(Projections.fields(ProblemSummaryQuery.class,
                        problem.id.as("problemId"),
                        problem.title.as("title"),
                        problem.submissionCount.as("submissionCount"),
                        problem.submitterCount.as("submitterCount"),
                        problemImage.imageKey.as("iconKey")
                ))
                .from(problem)
                .leftJoin(problem.iconKey, problemImage);

        Sort sort = pageable.getSortOr(Sort.by(Sort.Order.asc("id"))); // 비어있으면 id ASC
        query.orderBy(createOrderSpecifiers(sort));

        // 페이지네이션
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<ProblemSummaryQuery> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(problem.count())
                .from(problem);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] createOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> specifiers = new ArrayList<>();
        boolean hasIdSort = false;

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;    // 오름차순, 내림차순
            switch (order.getProperty()) {
                case "title":
                    specifiers.add(new OrderSpecifier<>(direction, problem.title));
                    break;
                case "submissionCount":
                    specifiers.add(new OrderSpecifier<>(direction, problem.submissionCount));
                    break;
                case "submitterCount":
                    specifiers.add(new OrderSpecifier<>(direction, problem.submitterCount));
                    break;
                case "createdAt":
                    specifiers.add(new OrderSpecifier<>(direction, problem.createdAt));
                    break;
                case "id":
                    hasIdSort = true;
                    specifiers.add(new OrderSpecifier<>(direction, problem.id));
                    break;
                default:
            }
        }

        if (specifiers.isEmpty()) {
            specifiers.add(problem.id.asc());
        } else if (!hasIdSort) {
            specifiers.add(problem.id.asc());
        }

        return specifiers.toArray(new OrderSpecifier<?>[0]);   // List를 Array로 변환
    }

    public List<HomeProblemQuery> findPopularProblems(int limit) {
        QProblem problem = QProblem.problem;
        QProblemImage problemImage = QProblemImage.problemImage;
        JPQLQuery<String> topIdentifier = getTopIdentifier(problem);

        return queryFactory
                .select(Projections.constructor(HomeProblemQuery.class,
                        problem.id,
                        problem.title,
                        problem.submissionCount,
                        problem.submitterCount,
                        topIdentifier,
                        problem.description,
                        problemImage.imageKey
                ))
                .from(problem)
                .leftJoin(problem.iconKey, problemImage)
                .orderBy(problem.submissionCount.desc())    // 제출 수가 가장 많은 문제
                .limit(limit)
                .fetch();
    }

    public HomeProblemQuery findRecentProblem() {
        QProblem problem = QProblem.problem;
        QProblemImage problemImage = QProblemImage.problemImage;
        JPQLQuery<String> topIdentifier = getTopIdentifier(problem);


        return queryFactory
                .select(Projections.constructor(HomeProblemQuery.class,
                        problem.id,
                        problem.title,
                        problem.submissionCount,
                        problem.submitterCount,
                        topIdentifier,
                        problem.description,
                        problemImage.imageKey
                ))
                .from(problem)
                .leftJoin(problem.iconKey, problemImage)
                .orderBy(problem.createdAt.desc())
                .limit(1)
                .fetchOne();
    }

    private JPQLQuery<String> getTopIdentifier(QProblem problem) {
        QUserRating ur = QUserRating.userRating;

        QUserRating urForMaxRating = new QUserRating("urForMaxRating");
        QUserRating urForMaxWin = new QUserRating("urForMaxWin");
        QUserRating urForEarliest = new QUserRating("urForEarliest");

        // 최고 레이팅
        JPQLQuery<Integer> maxRating =
                JPAExpressions.select(urForMaxRating.rating.max())
                        .from(urForMaxRating)
                        .where(urForMaxRating.problem.eq(problem));

        // 최고 승수
        JPQLQuery<Integer> maxWinAtMaxRating =
                JPAExpressions.select(urForMaxWin.win.max())
                        .from(urForMaxWin)
                        .where(
                                urForMaxWin.problem.eq(problem),
                                urForMaxWin.rating.eq(maxRating)
                        );

        // 가장 이른 시각
        JPQLQuery<LocalDateTime> earliestAtTie =
                JPAExpressions.select(urForEarliest.updatedAt.min())
                        .from(urForEarliest)
                        .where(
                                urForEarliest.problem.eq(problem),
                                urForEarliest.rating.eq(maxRating),
                                urForEarliest.win.eq(maxWinAtMaxRating)
                        );

        return JPAExpressions.select(ur.user.identifier.min())
                .from(ur)
                .where(
                        ur.problem.eq(problem),
                        ur.rating.eq(maxRating),
                        ur.win.eq(maxWinAtMaxRating),
                        ur.updatedAt.eq(earliestAtTie)
                );
    }
}
