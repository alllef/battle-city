package com.github.alllef.battle_city.core.util.enums;

import java.util.Optional;

import static com.badlogic.gdx.Input.Keys;

public enum Direction {
    LEFT(90), RIGHT(270), UP(0), DOWN(180);
    private final int degree;

    public static Optional<Direction> of(int key) {
        Direction dir = null;

        switch (key) {
            case Keys.UP -> dir = UP;
            case Keys.DOWN -> dir = DOWN;
            case Keys.LEFT -> dir = LEFT;
            case Keys.RIGHT -> dir = RIGHT;
        }

        return Optional.ofNullable(dir);
    }

    Direction(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }
}
