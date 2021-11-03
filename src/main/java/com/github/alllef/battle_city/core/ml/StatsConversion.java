package com.github.alllef.battle_city.core.ml;

import org.apache.commons.math3.stat.StatUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StatsConversion {
    private final File file;
    private final DataTransform transform = DataTransform.INSTANCE;

    private final List<Integer> time = new ArrayList<>();
    private final List<Integer> score = new ArrayList<>();
    private final List<String> finalResult = new ArrayList<>();
    private final List<String> algoType = new ArrayList<>();

    private int controlPart;
    private int rowsNum;

    public StatsConversion(File file, int controlPart) {
        this.file = file;
        this.controlPart = controlPart;
    }

    private void parseData() {
        try {
            FileReader reader = new FileReader(file);
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                String[] gameResults = scanner.nextLine().split(",");
                time.add(Integer.parseInt(gameResults[0]));
                score.add(Integer.parseInt(gameResults[1]));
                finalResult.add(gameResults[2]);
                algoType.add(gameResults[3]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        rowsNum = time.size();
    }

    public double[][] getNormalizedTrainingDependentVars() {
        double[] normalizedTime = transform.normalizeIntData(time);
        double[] normalizedFinalResult = transform.normalizeStringData(finalResult);
        double[] normalizeAlgoType = transform.normalizeStringData(algoType);

        double[][] normalizedResults = new double[normalizedTime.length][4];
        for (int i = 0; i < rowsNum - controlPart; i++)
            normalizedResults[i] = new double[]{normalizedTime[i], normalizedFinalResult[i], normalizeAlgoType[i]};

        return normalizedResults;
    }

    double getMean() {
        return StatUtils.mean(getNormalizedTrainingDependentVar());
    }

    public double[] getNormalizedTrainingDependentVar() {
        double[] trainPartResults = new double[rowsNum - controlPart];
        return Arrays.copyOf(transform.normalizeIntData(score), rowsNum - controlPart);
    }


    public double[] getDependentControlVar (){
        return Arrays.copyOfRange(score.stream().mapToDouble(i -> i).toArray(),rowsNum-controlPart,rowsNum);
    }

    public int getControlPart() {
        return controlPart;
    }

    public void setControlPart(int controlPart) {
        this.controlPart = controlPart;
    }
}
