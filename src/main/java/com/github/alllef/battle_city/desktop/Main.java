package com.github.alllef.battle_city.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.alllef.battle_city.core.BattleCity;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "Battle city";
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new BattleCity(), config);
    }
}
