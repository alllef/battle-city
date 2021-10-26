package com.github.alllef.battle_city.core.world.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.util.interfaces.Drawable;

public class ScoreManipulation implements Drawable {

    private final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    private final BitmapFont font = BitMapFontSingleton.getInstance();

    private int score = 0;
    private final float scoreScaleX;
    private final float scoreScaleY;
    private float scorePos;

    public ScoreManipulation() {
        scoreScaleX = prefs.getFloat("score_scale_X");
        scoreScaleY = prefs.getFloat("score_scaly_Y");
        scorePos = prefs.getInteger("world_size") * prefs.getFloat("score_pos");
    }

    public void tankKilled() {
        score += prefs.getInteger("killed_tank_score");
    }

    public int getScore() {
        return score;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        font.getData().setScale(prefs.getFloat("score_scale_X"), prefs.getFloat("score_scale_Y"));
        font.draw(spriteBatch, "Score: " + score, scorePos, scorePos);
    }
}
