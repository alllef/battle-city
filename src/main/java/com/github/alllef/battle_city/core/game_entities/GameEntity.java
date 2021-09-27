package com.github.alllef.battle_city.core.game_entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class GameEntity {
    protected Sprite sprite = new Sprite();

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameEntity)) return false;

        GameEntity that = (GameEntity) o;

        return getSprite() != null ? getSprite().equals(that.getSprite()) : that.getSprite() == null;
    }

    @Override
    public int hashCode() {
        return getSprite() != null ? getSprite().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GameEntity{" +
                "sprite=" + sprite +
                '}';
    }
}
