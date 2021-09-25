package com.github.alllef.battle_city.core.game_entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameEntity {
    protected Sprite sprite = new Sprite();

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
