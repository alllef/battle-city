package com.github.alllef.battle_city.core.ml;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.Arrays;

public class RegressionTest {
    OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
    public void playWithRegression(){
        double[] y = new double[]{11.0, 12.0, 13.0, 14.0, 15.0, 16.0};
        double[][] x = new double[6][];
        x[0] = new double[]{0, 0, 0, 0, 0};
        x[1] = new double[]{2.0, 0, 0, 0, 0};
        x[2] = new double[]{0, 3.0, 0, 0, 0};
        x[3] = new double[]{0, 0, 4.0, 0, 0};
        x[4] = new double[]{0, 0, 0, 5.0, 0};
        x[5] = new double[]{0, 0, 0, 0, 6.0};
        regression.newSampleData(y, x);
        System.out.println(Arrays.toString(regression.estimateRegressionParameters()));
        System.out.println(StatUtils.normalize(x[4]));

    }

    public static void main(String[]args){
        RegressionTest test = new RegressionTest();
        test.playWithRegression();
    }
}
