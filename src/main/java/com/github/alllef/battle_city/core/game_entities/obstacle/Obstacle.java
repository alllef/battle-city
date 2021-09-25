package com.github.alllef.battle_city.core.game_entities.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entities.GameEntity;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Obstacle extends GameEntity {

    public Obstacle(int x, int y) {
        sprite.setTexture(new Texture(Gdx.files.internal(SpriteParam.OBSTACLE.getTexturePath())));
        sprite.setSize(SpriteParam.OBSTACLE.getWidth(), SpriteParam.OBSTACLE.getHeight());
        sprite.setPosition(x, y);
    }
}
