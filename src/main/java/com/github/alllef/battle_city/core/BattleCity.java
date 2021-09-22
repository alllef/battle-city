package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class BattleCity extends Game {

    @Override
    public void create() {
        this.setScreen(new MainScreen());
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        prefs.putInteger("world_size",100);
        prefs.putFloat("min_change_distance",1.0f / 10f);

    }

    @Override
    public void render() {
        super.render();
    }


}
