package com.github.alllef.battle_city.core.util.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    public static List<Direction> shuffleDirections() {
        Direction[] directions = Direction.values();
        List<Direction> shuffledDirections = new ArrayList<>();
        Random rand = new Random();
        while (shuffledDirections.size() < directions.length) {
            Direction tmp = directions[rand.nextInt(directions.length)];
            if (!shuffledDirections.contains(tmp))
                shuffledDirections.add(tmp);
        }
        return shuffledDirections;
    }

    Direction(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }
}
