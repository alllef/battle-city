package com.github.alllef.battle_city.core.ml;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleRegression {
    OLSMultipleLinearRegression olsMultipleLinearRegression = new OLSMultipleLinearRegression();
    private double[][] x;
    private double[] y;

    public MultipleRegression(double[] y, double[][] x) {
        olsMultipleLinearRegression.newSampleData(y, x);
        this.x = x;
        this.y = y;
    }

    public double[][] calcNextRegressionParameters(int paramsNumber) {

        double[][] nextParams = new double[paramsNumber][olsMultipleLinearRegression.estimateRegressionParameters().length];
        List<double[]> tmpXList = Arrays.stream(x)
                .collect(Collectors.toList());

        List<Double> tmpYList = Arrays.stream(y)
                .boxed()
                .collect(Collectors.toList());

        for (int i = 0; i < paramsNumber; i++) {
            nextParams[i] = olsMultipleLinearRegression.estimateRegressionParameters();

            tmpXList.add(new double[]{nextParams[i][1], nextParams[i][2], nextParams[i][3]});
            tmpYList.add(nextParams[i][0]);

            double[] tmpYArr;
            double[][] tmpXArr = new double[3][tmpXList.size()];
            tmpYArr = tmpYList.stream().mapToDouble(tmp -> tmp).toArray();
            tmpXList.toArray(tmpXArr);

            olsMultipleLinearRegression.newSampleData(tmpYArr, tmpXArr);
        }

        return nextParams;
    }
}
