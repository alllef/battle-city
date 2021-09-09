package com.github.alllef.space_invaders.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.alllef.space_invaders.core.SpaceInvaders;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "Space Invaders";
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new SpaceInvaders(), config);
    }
}
