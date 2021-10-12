package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.screen.MainScreen;

public class BattleCity extends Game {

    @Override
    public void create() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        prefs.putInteger("world_size",100);
        prefs.putFloat("min_change_distance",prefs.getInteger("world_size")/1000f);
        prefs.putInteger("bullets_cooldown",500);
        prefs.putFloat("bullet_speed_scaled",2.0f);
        prefs.putInteger("killed_tank_score",100);
        prefs.putFloat("score_scale_X",0.15f);
        prefs.putFloat("score_scale_Y",0.25f);
        prefs.putFloat("score_pos",0.8f);
        prefs.putInteger("min_obstacle_set_size",10);
        prefs.putInteger("obstacle_size_dispersion",15);
        prefs.putInteger("max_ride_distance",40);
        prefs.putInteger("enemy_tanks_number",5);
        prefs.putInteger("obstacle_sets",20);
        prefs.putBoolean("enable_shooting",false);
        this.setScreen(new MainScreen());
    }

    @Override
    public void render() {
        super.render();
    }


}
