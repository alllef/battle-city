package com.github.alllef.battle_city.core.game_entity.tank;

import com.github.alllef.battle_city.core.util.enums.Direction;

public interface Tank {
    void shoot();

    void ride(Direction dir);
}
