package com.github.alllef.battle_city.core.ml;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.File;

public class RegressionForecasting {
    DataTransform transform = DataTransform.INSTANCE;

    public void forecast(File readFile, File writeFile) {
        StatsConversion statsConversion = new StatsConversion(readFile, 10);
        MultipleRegression regression = new MultipleRegression(statsConversion.getNormalizedTrainingDependentVar(), statsConversion.getNormalizedTrainingDependentVars());
        StandardDeviation deviation = new StandardDeviation();

        double[] expectedYVars = transform.denormalizeData(regression.calcNextDependent(10), (int) statsConversion.getMinScore(), (int) statsConversion.getMaxScore());
        double[] actualVars = statsConversion.getDependentControlVar();

        double[] expectedDeviations = regression.getDeviations(expectedYVars, actualVars);

        double[] actualDeviations = new double[expectedDeviations.length];

        double mean = statsConversion.getMean();
        for (int i = 0; i < actualDeviations.length; i++)
            actualDeviations[i] = Math.sqrt(Math.pow(actualVars[i] - mean, 2));

        ForecastResultsWriter writer = new ForecastResultsWriter(writeFile);
        writer.writeData(expectedYVars, actualVars, expectedDeviations, actualDeviations);
    }
}
