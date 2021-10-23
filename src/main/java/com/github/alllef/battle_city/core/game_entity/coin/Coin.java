package com.github.alllef.battle_city.core.game_entity.coin;

import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Coin extends GameEntity {
    public Coin(float posX, float posY) {
        super(posX,posY,SpriteParam.COIN);
    }

}
