package com.github.alllef.battle_city.core.util;

import java.util.function.ToDoubleBiFunction;

public record Coords(int x, int y) {

    public float calcCoordDist(Coords otherCoord) {
        ToDoubleBiFunction<Integer, Integer> dist = (first, second) -> Math.pow(first - second, 2.0d);

        float xDist = (float) dist.applyAsDouble(this.x(), otherCoord.x());
        float yDist = (float) dist.applyAsDouble(this.y(), y());
        return (float) Math.sqrt(xDist + yDist);
    }

    public float calcManhattanDist(Coords second) {
        float xDist = this.x() - second.x();
        float yDist = this.y() - second.y();
        if (xDist < 0) xDist = -xDist;
        if (yDist < 0) yDist = -yDist;
        return xDist + yDist;
    }
}