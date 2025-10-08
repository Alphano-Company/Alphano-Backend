package com.alphano.alphano.common.util;

import com.alphano.alphano.domain.match.domain.MatchResult;

public class RatingUtils {
    private static final double S = 800.0;
    private static final double OFFSET = 1500.0 + 0.5108256 * S;
    private static final int K_FACTOR = 32;

    /**
     * Elo 레이팅 계산 결과를 담는 Record
     * @param newRating1 Agent1의 새로운 레이팅
     * @param newRating2 Agent2의 새로운 레이팅
     */
    public record EloResult(double newRating1, double newRating2) {}

    /**
     * 새로운 Elo 레이팅을 계산
     * @param rating1 Agent1의 기존 레이팅
     * @param rating2 Agent2의 기존 레이팅
     * @param result 경기 결과
     * @return 계산된 EloResult 객체
     */
    public static EloResult calculateNewRating(double rating1, double rating2, MatchResult result) {
        double expectedWinRate1 = 1.0 / (1.0 + Math.pow(10, (rating2 - rating1) / 400.0));
        double expectedWinRate2 = 1.0 - expectedWinRate1;

        double actualScore1 = getActualScore(result);
        double actualScore2 = 1.0 - actualScore1;

        double newRating1 = rating1 + K_FACTOR * (actualScore1 - expectedWinRate1);
        double newRating2 = rating2 + K_FACTOR * (actualScore2 - expectedWinRate2);

        return new EloResult(newRating1, newRating2);
    }


    /**
     * 경기 결과에 따른 Agent1의 실제 점수를 반환합니다. (승: 1, 무: 0.5, 패: 0)
     */
    private static double getActualScore(MatchResult result) {
        return switch (result) {
            case AGENT1_WIN -> 1.0;
            case DRAW -> 0.5;
            case AGENT2_WIN -> 0.0;
        };
    }


    public static double convert(double x) {
        double z = (x - OFFSET) / S;
        double sigma = 1.0 / (1.0 + Math.exp(-z));
        return 4000.0 * sigma;
    }
}