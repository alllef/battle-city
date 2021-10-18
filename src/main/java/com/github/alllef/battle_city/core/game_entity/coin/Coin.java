package com.github.alllef.battle_city.core.game_entity.coin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Coin extends GameEntity {
    public Coin(float posX, float posY) {
        SpriteParam param = SpriteParam.COIN;
        sprite = new Sprite(new Texture(param.getTexturePath()));
        sprite.setSize(param.getWidth(), param.getHeight());
        sprite.setPosition(posX, posY);
    }

}
