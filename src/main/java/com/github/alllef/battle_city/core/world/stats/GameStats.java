package com.github.alllef.battle_city.core.world.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.util.enums.GameResult;

import java.io.FileWriter;
import java.io.IOException;

public class GameStats {
    private static GameStats gamestats;

    public static GameStats getInstance() {
        if (gamestats == null)
            gamestats = new GameStats();
        return gamestats;
    }

    ScoreManipulation scoreManipulation = ScoreManipulation.INSTANCE;
    Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    private long startTime = TimeUtils.millis();
    private int scoreNumber;
    private GameResult result;
    private String algorithm;
    private boolean isGameOver = false;

    private void onGameOver() {
        if (isGameOver) {
            try (FileWriter writer = new FileWriter(prefs.getString("stats.csv"), true)) {
                writer.write(TimeUtils.millis() - startTime + "," + scoreNumber + "," + result + "," + algorithm + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(GameResult gameResult) {
        this.result = gameResult;
        this.scoreNumber = scoreManipulation.getScore();
        isGameOver = true;
        onGameOver();
    }
}
