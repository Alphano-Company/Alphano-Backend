package com.alphano.alphano.common.util;

public class RatingUtils {
    private static final double S = 800.0;
    private static final double OFFSET = 1500.0 + 0.5108256 * S;

    public static double convert(double x) {
        double z = (x - OFFSET) / S;
        double sigma = 1.0 / (1.0 + Math.exp(-z));
        return 4000.0 * sigma;
    }
}