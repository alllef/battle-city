package com.github.alllef.battle_city.core.game_entity.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Obstacle extends GameEntity {

    public Obstacle(int x, int y) {
       super();
    }

    @Override
    protected Sprite spriteConfigure() {
        SpriteParam param = SpriteParam.OBSTACLE;
        Sprite tmp = new Sprite(new Texture(Gdx.files.internal(param.getTexturePath())));
        sprite.setSize(param.getWidth(), param.getHeight());
        sprite.setPosition(x, y);
        return tmp;
    }
}
