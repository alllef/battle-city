package com.github.alllef.battle_city.core.game_entities.tank;

import com.github.alllef.battle_city.core.util.Direction;

public interface Tank {
    void shoot();

    void ride(Direction dir);
}
