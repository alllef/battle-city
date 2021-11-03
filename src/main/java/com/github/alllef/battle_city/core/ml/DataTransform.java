package com.github.alllef.battle_city.core.ml;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.ranking.NaturalRanking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DataTransform {
    INSTANCE;

    public double[] normalizeIntData(List<Integer> data) {
        return StatUtils.normalize(data.stream().mapToDouble(i -> i).toArray());
    }

    public double[] normalizeStringData(List<String> data) {
        String[] dataArr = new String[data.size()];
        data.toArray(dataArr);

        Map<String, Integer> rankedStrings = new HashMap<>();
        double[] rankedArr = new double[dataArr.length];
        int rank = 0;

        for (int i = 0; i < dataArr.length; i++) {
            if (rankedStrings.containsKey(dataArr[i]))
                rankedArr[i] = rankedStrings.get(dataArr[i]);
            else
                rankedStrings.put(dataArr[i], rank++);
        }

        return StatUtils.normalize(rankedArr);
    }

    public double[] denormalizeData(double[] normalizedData, int min, int max) {
        double[] originalData = new double[normalizedData.length];
        int coef = (max - min) / 2;

        for (int i = 0; i < normalizedData.length; i++) {
            originalData[i] = (normalizedData[i] + 2) * coef;
        }

        return originalData;
    }
}
