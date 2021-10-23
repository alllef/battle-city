package com.github.alllef.battle_city.core.game_entity.obstacle;

import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;

public class Obstacle extends GameEntity {

    public Obstacle(float posX, float posY) {
       super(posX,posY,SpriteParam.OBSTACLE);
    }

}
