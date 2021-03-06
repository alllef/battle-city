package com.github.alllef.battle_city.core.ml;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleRegression {
    private final OLSMultipleLinearRegression olsMultipleLinearRegression = new OLSMultipleLinearRegression();
    private double[][] x;
    private double[] y;

    public MultipleRegression(double[] y, double[][] x) {
        olsMultipleLinearRegression.newSampleData(y, x);
        this.x = x;
        this.y = y;
    }

    public double[] calcNextDependent(int dependentNumber) {

        double[][] nextParams = new double[dependentNumber][olsMultipleLinearRegression.estimateRegressionParameters().length];
        List<double[]> tmpXList = Arrays.stream(x)
                .collect(Collectors.toList());

        List<Double> tmpYList = Arrays.stream(y)
                .boxed()
                .collect(Collectors.toList());

        for (int i = 0; i < dependentNumber; i++) {
            nextParams[i] = olsMultipleLinearRegression.estimateRegressionParameters();

            tmpXList.add(new double[]{nextParams[i][1], nextParams[i][2], nextParams[i][3]});
            tmpYList.add(nextParams[i][0]);

            double[] tmpYArr;
            double[][] tmpXArr = new double[tmpXList.size()][3];
            tmpYArr = tmpYList.stream().mapToDouble(tmp -> tmp).toArray();
            tmpXList.toArray(tmpXArr);

            olsMultipleLinearRegression.newSampleData(tmpYArr, tmpXArr);
        }

        double[] yVariables = new double[nextParams.length];
        for (int i = 0; i < yVariables.length; i++)
            yVariables[i] = nextParams[i][0];

        return yVariables;
    }

    double[] getDeviations(double[] expected, double[] actual) {
        double[] deviations = new double[expected.length];

        for (int i = 0; i < expected.length; i++)
            deviations[i] = Math.sqrt(Math.pow(expected[i] - actual[i], 2));

        return deviations;
    }
}
