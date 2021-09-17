package com.github.alllef.battle_city.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Optional;

import static com.badlogic.gdx.Input.Keys;

public enum Direction {
    LEFT, RIGHT, UP, DOWN;

    public static Optional<Direction> of(int key) {
        Direction dir = null;

        switch (key) {
            case Keys.UP -> dir = UP;
            case Keys.DOWN -> dir = DOWN;
            case  Keys.LEFT -> dir = LEFT;
            case Keys.RIGHT -> dir = RIGHT;
        }

        return Optional.ofNullable(dir);
    }
}
