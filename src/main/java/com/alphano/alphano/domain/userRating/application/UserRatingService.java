package com.alphano.alphano.domain.userRating.application;

import com.alphano.alphano.common.util.RatingUtils;
import com.alphano.alphano.domain.match.dao.MatchRepository;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.match.domain.MatchResult;
import com.alphano.alphano.domain.match.exception.MatchNotFoundException;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.user.dao.UserRepository;
import com.alphano.alphano.domain.user.domain.User;
import com.alphano.alphano.domain.user.exception.UserNotFoundException;
import com.alphano.alphano.domain.userHistory.dao.UserHistoryRepository;
import com.alphano.alphano.domain.userHistory.domain.Outcome;
import com.alphano.alphano.domain.userHistory.domain.UserHistory;
import com.alphano.alphano.domain.userRating.dao.UserRatingRepository;
import com.alphano.alphano.domain.userRating.domain.UserRating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRatingService {


    private final UserRatingRepository userRatingRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final MatchRepository matchRepository;
    private final UserHistoryRepository userHistoryRepository;

    @Transactional
    public void applyResult(Long matchId, Long problemId, MatchResult result, Long agent1UserId, Long agent2UserId) {
        User agent1User = userRepository.findById(agent1UserId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        User agent2User = userRepository.findById(agent2UserId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> ProblemNotFoundException.EXCEPTION);
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> MatchNotFoundException.EXCEPTION);

        UserRating rating1 = findOrCreateUserRating(agent1User, problem);
        UserRating rating2 = findOrCreateUserRating(agent2User, problem);

        double ratingBefore1 = rating1.getRating();
        double ratingBefore2 = rating2.getRating();

        Outcome outcome1 = toOutcome(result, agent1UserId, agent1UserId, agent2UserId);
        Outcome outcome2 = toOutcome(result, agent2UserId, agent1UserId, agent2UserId);

        RatingUtils.EloResult eloResult = RatingUtils.calculateNewRating(ratingBefore1, ratingBefore2, result);

        rating1.updateRating(eloResult.newRating1());
        rating2.updateRating(eloResult.newRating2());

        updateStatsByOutcome(rating1, outcome1);
        updateStatsByOutcome(rating2, outcome2);

        createUserHistory(match, agent1User, problem, ratingBefore1, eloResult.newRating1(), agent2User.getId(), outcome1);
        createUserHistory(match, agent2User, problem, ratingBefore2, eloResult.newRating2(), agent1User.getId(), outcome2);
    }

    /**
     * 주어진 유저와 문제에 대한 UserRating을 비관적 락을 걸어 조회
     * 존재하지 않을 경우 새로 생성하여 저장
     * @param user 레이팅을 조회할 유저
     * @param problem 레이팅과 관련된 문제
     * @return 조회되거나 새로 생성된 UserRating 엔티티
     */
    private UserRating findOrCreateUserRating(User user, Problem problem) {
        return userRatingRepository.findByProblemIdAndUserIdForUpdate(problem.getId(), user.getId())
                .orElseGet(() -> {
                    UserRating newUserRating = UserRating.create();
                    user.addUserRating(newUserRating);
                    problem.addUserRating(newUserRating);
                    return userRatingRepository.save(newUserRating);
                });
    }

    /**
     * 경기 결과(승/무/패)에 따라 UserRating의 통계(승/무/패, 연승)를 업데이트
     * @param rating 업데이트할 UserRating 엔티티
     * @param outcome 해당 유저의 경기 결과 (WIN, LOSE, DRAW)
     */
    private void updateStatsByOutcome(UserRating rating, Outcome outcome) {
        switch (outcome) {
            case WIN -> rating.applyWin();
            case LOSE -> rating.applyLose();
            case DRAW -> rating.applyDraw();
        }
    }

    /**
     * 전체 매치 결과를 바탕으로 특정 유저 관점에서의 개별 결과(승/무/패)를 결정
     * @param result 매치의 전체 결과 (AGENT1_WIN, AGENT2_WIN, DRAW)
     * @param who 결과를 판단할 관점 유저의 ID
     * @param agent1 에이전트 1의 ID
     * @param agent2 에이전트 2의 ID
     * @return 유저의 개별 결과 (WIN, LOSE, DRAW)
     */
    protected Outcome toOutcome(MatchResult result, Long who, Long agent1, Long agent2) {
        return switch (result) {
            case DRAW       -> Outcome.DRAW;
            case AGENT1_WIN -> who.equals(agent1) ? Outcome.WIN : Outcome.LOSE;
            case AGENT2_WIN -> who.equals(agent2) ? Outcome.WIN : Outcome.LOSE;
        };
    }

    /**
     * 유저의 매치 기록에 대한 UserHistory 엔티티를 생성하고 저장
     * @param match 기록을 생성할 매치 엔티티
     * @param user 기록의 대상이 되는 유저
     * @param problem 매치가 진행된 문제
     * @param before 경기 전 레이팅
     * @param after 경기 후 레이팅
     * @param opponentId 상대방 유저의 ID
     * @param outcome 해당 유저의 경기 결과
     */
    private void createUserHistory(Match match, User user, Problem problem, double before, double after, Long opponentId, Outcome outcome) {
        UserHistory userHistory = UserHistory.create(match, user, problem, before, after, opponentId, outcome);
        userHistoryRepository.save(userHistory);
    }
}
