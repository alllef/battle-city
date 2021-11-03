package com.github.alllef.battle_city.core.ml;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.File;

public class RegressionForecasting {
    DataTransform transform = DataTransform.INSTANCE;

    public void forecast(File file) {
        StatsConversion statsConversion = new StatsConversion(file, 10);
        MultipleRegression regression = new MultipleRegression(statsConversion.getNormalizedTrainingDependentVar(), statsConversion.getNormalizedTrainingDependentVars());
        StandardDeviation deviation = new StandardDeviation();

        double[] nextYVars = transform.denormalizeData(regression.calcNextDependent(5));
        double[] expectedDeviations = regression.getDeviations(nextYVars, statsConversion.getMean());

        double[] realDeviations = new double[expectedDeviations.length];
        double[] controlVars = statsConversion.getDependentControlVar();

        for (int i = 0; i < realDeviations.length; i++)
            realDeviations[i] = deviation.evaluate(new double[]{nextYVars[i]}, controlVars[i]);


      }
}
