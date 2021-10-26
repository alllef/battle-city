package com.github.alllef.battle_city.core.world.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.util.enums.GameResult;

import java.io.FileWriter;
import java.io.IOException;

public class GameStats {

    private final ScoreManipulation scoreManipulation;
    Preferences prefs;

    private long startTime = TimeUtils.millis();
    private int scoreNumber;
    private GameResult result;
    private String algorithm;
    private boolean isGameOver = false;

    public GameStats(ScoreManipulation scoreManipulation,Preferences prefs) {
        this.prefs=prefs;
        this.scoreManipulation = scoreManipulation;
    }

    private void onGameOver() {
        if (isGameOver) {
            try (FileWriter writer = new FileWriter(prefs.getString("stats_file"), true)) {
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
