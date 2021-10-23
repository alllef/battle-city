package com.github.alllef.battle_city.core.util;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;

public class RectUtils {

    public static RectangleFloat getSmallestRect(Coords coords) {
        return getFloatRect(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1);
    }

    public static RectangleFloat getFloatRect(float x1, float y1, float x2, float y2) {
        return (RectangleFloat) Geometries.rectangle(x1, y1, x2, y2);
    }
}
