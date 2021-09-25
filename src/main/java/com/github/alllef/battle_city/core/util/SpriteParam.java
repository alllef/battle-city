package com.github.alllef.battle_city.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public enum SpriteParam {
    OBSTACLE(1 / 50, 1 / 100, "sprites/block.png"),
    BULLET(1 / 50, 1 / 50, "sprites/bullet.png"),
    SINGLE_TANK(1/50,1/50,""),
    PLAYER_TANK(1 / 50, 1 / 50, "sprites/player.png"),
    ENEMY_TANK(1 / 50, 1 / 50, "sprites/enemy.png");

    private final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    private final int width, height;
    private final String texturePath;

    SpriteParam(int width, int height, String texturePath) {
        int worldSize = prefs.getInteger("world_size");
        this.width = width * worldSize;
        this.height = height * worldSize;
        this.texturePath = texturePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
