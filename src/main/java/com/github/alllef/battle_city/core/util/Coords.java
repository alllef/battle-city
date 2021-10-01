package com.github.alllef.battle_city.core.util;

public record Coords(int x, int y) {

    public float calculateDistance(Coords secondCoords) {
        float xDistance = getDistance(this.x(), secondCoords.x());
        float yDistance = getDistance(this.y(),y());
       return (float) Math.sqrt(xDistance+yDistance);
    }

    private float getDistance(int first, int second) {
        return (float) Math.pow(first - second,2.0d);
    }
}