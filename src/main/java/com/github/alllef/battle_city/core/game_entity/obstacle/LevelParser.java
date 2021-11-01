package com.github.alllef.battle_city.core.game_entity.obstacle;

import com.github.alllef.battle_city.core.util.Coords;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LevelParser {
    private final File levelFile;

    public LevelParser(String fileName) {
        this.levelFile = new File(fileName);
    }

    public List<Coords> parseFile() {
        List<Coords> blockCoords = new ArrayList<>();
        int x = 0;
        try (FileReader reader = new FileReader(levelFile)) {
            Scanner levelScanner = new Scanner(reader);
            while (levelScanner.hasNextLine()) {
                blockCoords.addAll(getLineCoords(x, levelScanner.nextLine()));
                x++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blockCoords;
    }

    private List<Coords> getLineCoords(int x, String line) {
        List<Coords> lineCoords = new ArrayList<>();
        char[] charArr = line.toCharArray();

        for (int i = 0; i < charArr.length; i++) {
            if (charArr[i] == 'X')
                lineCoords.add(new Coords(x, i));
        }
        return lineCoords;
    }
}
