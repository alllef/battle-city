package com.github.alllef.battle_city.core.ml;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.ranking.NaturalRanking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataTransform {

    public double[] normalizeIntData(int[] data) {
        return StatUtils.normalize((Arrays.stream(data).asDoubleStream().toArray()));
    }

    public double[] normalizeStringData(String[] data) {
        Map<String, Integer> rankedStrings = new HashMap<>();
        double[] rankedArr = new double[data.length];
        int rank = 0;

        for (int i = 0; i < data.length; i++) {
            if (rankedStrings.containsKey(data[i]))
                rankedArr[i] = rankedStrings.get(data[i]);
            else
                rankedStrings.put(data[i],rank++);
        }

        return StatUtils.normalize(rankedArr);
    }
}
