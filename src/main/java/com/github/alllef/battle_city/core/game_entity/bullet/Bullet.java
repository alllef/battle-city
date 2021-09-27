package com.github.alllef.battle_city.core.game_entity.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Bullet extends GameEntity {

    private Direction dir;

    protected Bullet(float x, float y, Direction dir) {
        this.dir = dir;
        SpriteParam param = SpriteParam.BULLET;
        sprite = new Sprite(new Texture(param.getTexturePath()));
        sprite.setSize(param.getWidth(), param.getHeight());
        sprite.setOriginCenter();
        sprite.setRotation(dir.getDegree());
        sprite.setPosition(x, y);

    }

    public void move() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");

        float minDistance = prefs.getFloat("min_change_distance")* prefs.getFloat("bullet_speed_scaled");

        switch (dir) {
            case UP -> sprite.setY(sprite.getY() + minDistance);
            case DOWN -> sprite.setY(sprite.getY() - minDistance);
            case RIGHT -> sprite.setX(sprite.getX() + minDistance);
            case LEFT -> sprite.setX(sprite.getX() - minDistance);
        }
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

}
