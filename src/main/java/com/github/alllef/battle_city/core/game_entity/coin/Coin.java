package com.github.alllef.battle_city.core.game_entity.coin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Coin extends GameEntity {
    public Coin(float posX, float posY) {
        super();
    }

    @Override
    protected Sprite spriteConfigure() {
        SpriteParam param = SpriteParam.COIN;
        Sprite tmp = new Sprite(new Texture(param.getTexturePath()));
        tmp.setSize(param.getWidth(), param.getHeight());
        tmp.setPosition(posX, posY);
        return tmp;
    }
}
