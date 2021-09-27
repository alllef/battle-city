package com.github.alllef.battle_city.core.util.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.util.Direction;

import java.awt.*;
import java.util.Optional;

public class WorldCheck {
    public static Optional<Direction> isOutOfWorld(Sprite sprite) {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        if (sprite.getX() > worldSize) return Optional.of(Direction.RIGHT);
        if (sprite.getX() < 0) return Optional.of(Direction.LEFT);
        if (sprite.getY() > worldSize) return Optional.of(Direction.UP);
        if (sprite.getY() < 0) return Optional.of(Direction.DOWN);
        return Optional.empty();
    }

    private WorldCheck() {
    }
}
