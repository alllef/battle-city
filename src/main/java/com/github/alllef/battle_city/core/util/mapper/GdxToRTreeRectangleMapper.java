package com.github.alllef.battle_city.core.util.mapper;

import com.badlogic.gdx.math.Rectangle;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;

public enum GdxToRTreeRectangleMapper {
    ENTITY;

    public Rectangle convertToGdxRectangle(RectangleFloat rTreeRectangle) {
        float x = rTreeRectangle.x1;
        if (x > rTreeRectangle.x2)
            x = rTreeRectangle.x2;

        float y = rTreeRectangle.y1;
        if (y > rTreeRectangle.y2)
            y = rTreeRectangle.y2;

        float width = rTreeRectangle.x2 - rTreeRectangle.x1;
        float height = rTreeRectangle.y2 - rTreeRectangle.y1;
        if (width < 0) width = -width;
        if (height < 0) height = -height;

        return new Rectangle(x, y, width, height);
    }

    public RectangleFloat convertToRTreeRectangle(Rectangle gdxRectangle) {
        float x1 = gdxRectangle.getX();
        float y1 = gdxRectangle.getY();
        float x2 = x1 + gdxRectangle.getWidth();
        float y2 = y1 + gdxRectangle.getHeight();
        return (RectangleFloat) Geometries.rectangle(x1, y1, x2, y2);
    }
}
