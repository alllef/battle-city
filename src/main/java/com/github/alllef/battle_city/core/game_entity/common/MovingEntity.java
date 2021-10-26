package com.github.alllef.battle_city.core.game_entity.common;

import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.enums.Move;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;

public class MovingEntity extends GameEntity {
    protected Direction dir;
    protected Preferences prefs;

    public MovingEntity(float posX, float posY, SpriteParam param, Direction dir, Preferences prefs) {
        super(posX, posY, param);
        this.dir = dir;
        this.prefs = prefs;
    }

    protected void move(Move type, float minDist) {
        if (type == Move.BACKWARD)
            minDist = -minDist;

        switch (this.getDir()) {
            case UP -> sprite.setY(sprite.getY() + minDist);
            case DOWN -> sprite.setY(sprite.getY() - minDist);
            case RIGHT -> sprite.setX(sprite.getX() + minDist);
            case LEFT -> sprite.setX(sprite.getX() - minDist);
        }
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }
}
