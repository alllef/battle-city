package com.github.alllef.battle_city.core.game_entity.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Bullet extends GameEntity {

    private Direction dir;

    protected Bullet(float posX, float posY, Direction dir) {
        super(posX, posY, SpriteParam.BULLET);
        this.dir = dir;
        sprite.setRotation(dir.getDegree());
    }

    public void move() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

        float minDistance = prefs.getFloat("min_change_distance") * prefs.getFloat("bullet_speed_scaled");

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

    @Override
    protected Sprite spriteConfigure(float posX, float posY, SpriteParam param) {
        Sprite tmp = super.spriteConfigure(posX, posY, param);
        tmp.setOriginCenter();
        return tmp;
    }

    @Override
    public String toString() {
        return "Bullet{" +
                "dir=" + dir +
                ", sprite=" + sprite +
                '}';
    }
}
