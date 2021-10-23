package com.github.alllef.battle_city.core.game_entity.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.common.MovingEntity;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.enums.Move;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;

public class Bullet extends MovingEntity {

    protected Bullet(float posX, float posY, Direction dir) {
        super(posX, posY, SpriteParam.BULLET,dir);
        sprite.setRotation(dir.getDegree());
    }

    public void move() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

        float minDistance = prefs.getFloat("min_change_distance") * prefs.getFloat("bullet_speed_scaled");
        this.move(Move.FORWARD, minDistance);
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
