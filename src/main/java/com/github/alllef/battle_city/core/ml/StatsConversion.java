package com.github.alllef.battle_city.core.ml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StatsConversion {
    private final File file;
    private final DataTransform transform = DataTransform.INSTANCE;

    public StatsConversion(File file) {
        this.file = file;
    }

    private double[][] getNormalizeData() {
        List<Integer> time = new ArrayList<>();
        List<Integer> score = new ArrayList<>();
        List<String> finalResult = new ArrayList<>();
        List<String> algoType = new ArrayList<>();

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

        double[] normalizedTime = transform.normalizeIntData(time);
        double[] normalizeScore = transform.normalizeIntData(score);
        double[] normalizedFinalResult = transform.normalizeStringData(finalResult);
        double[] normalizeAlgoType = transform.normalizeStringData(algoType);

        double[][] normalizedResults = new double[normalizedTime.length][4];
        for (int i = 0; i < normalizedTime.length; i++)
            normalizedResults[i] = new double[]{normalizeScore[i], normalizedTime[i], normalizedFinalResult[i], normalizeAlgoType[i]};


        return normalizedResults;
    }
}
