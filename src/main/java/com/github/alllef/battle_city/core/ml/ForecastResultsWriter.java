package com.github.alllef.battle_city.core.ml;

import com.badlogic.gdx.utils.TimeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ForecastResultsWriter {
    private File fileToWrite;

    public ForecastResultsWriter(File fileToWrite) {
        this.fileToWrite = fileToWrite;
    }

    public void writeData(double[] expected, double[] real, double[] expectedDeviation, double[] realDeviation) {

        try (FileWriter writer = new FileWriter(fileToWrite, false)) {
            writer.write("Expected_Score,Real_Score,Expected_Deviation,Real_Deviation\n");

            for (int i = 0; i < expected.length; i++)
                writer.write(expected[i] + "," + real[i] + "," + expectedDeviation[i] + "," + realDeviation[i] + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}