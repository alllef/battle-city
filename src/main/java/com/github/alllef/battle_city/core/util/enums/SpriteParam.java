package com.github.alllef.battle_city.core.util.enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public enum SpriteParam {
    OBSTACLE(1.0f / 50, 1.0f / 100, "sprites/block.png"),
    BULLET(1.0f / 50, 1.0f / 50, "sprites/bullet.png"),
    SINGLE_TANK(1.0f / 50, 1.0f / 50, ""),
    PLAYER_TANK(1.0f / 50, 1.0f / 50, "sprites/player.png"),
    ENEMY_TANK(1.0f / 50, 1.0f / 50, "sprites/enemy.png"),
    ENEMY_RANDOM_TANK(1.0f / 50, 1.0f / 50, "sprites/green_enemy.png"),
    ENEMY_PLAYER_TANK(1.0f / 50, 1.0f / 50, "sprites/purple_enemy.png"),
    COIN(1.0f/100,1.0f/100,"sprites/coin.png");

    private final float width, height;
    private final String texturePath;

    SpriteParam(float width, float height, String texturePath) {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        this.width = width * worldSize;
        this.height = height * worldSize;
        this.texturePath = texturePath;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
