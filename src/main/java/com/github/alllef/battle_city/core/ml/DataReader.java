package com.github.alllef.battle_city.core.ml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DataReader {
    private File file;

    public DataReader(File file) {
        this.file = file;
    }

    private double[][] getConvertData(){

        try {
            FileReader reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
