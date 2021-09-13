package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Game;

public class BattleCity extends Game {

    @Override
    public void create() {
        this.setScreen(new MainScreen());
    }

    @Override
    public void render() {
        super.render();
    }
}
