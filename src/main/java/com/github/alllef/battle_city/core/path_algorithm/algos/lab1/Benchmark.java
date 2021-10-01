package com.github.alllef.battle_city.core.path_algorithm.algos.lab1;

import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.ArrayList;
import java.util.List;

public class Benchmark {

    public static void main(String[] args) {
        long seconds = TimeUtils.millis();

    /*    Coords[][]matrix = new Coords[1000][1000];
        for (int i=0; i<matrix.length;i++){
            for(int j=0; j<matrix.length;j++){
                matrix[i][j] = new Coords(1,1);
            }
        }*/
        for (int i = 0; i < 100000000; i++) {
            List<Coords> list = new ArrayList<>();
        }

        System.out.println(TimeUtils.millis() - seconds);
    }

}
