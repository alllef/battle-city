package com.github.alllef.battle_city.core.game_entity.common;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.util.Drawable;

public abstract class GameEntity implements Drawable {
    protected Sprite sprite;

    public GameEntity() {
        this.sprite = spriteConfigure();
    }

    public Rectangle getRect() {
        return sprite.getBoundingRectangle();
    }



    protected abstract Sprite spriteConfigure();

    @Override
    public void draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

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
