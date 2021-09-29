package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.world.RTreeMap;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class PathAlgo {
    protected RTree<GameEntity, RectangleFloat> rTree = RTreeMap.getInstance().getrTree();
    protected final int worldSize;

    private Rectangle startRect;
    private Rectangle endRect;

    public PathAlgo(Rectangle startRect, Rectangle endRect) {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        worldSize = prefs.getInteger("world_size");
        this.startRect = startRect;
        this.endRect = endRect;
    }


    protected List<Coords> getAdjacentVertices(Coords coords) {
        List<Coords> adjacent = new LinkedList<>();

        Optional<Coords> upAdjacent = getAdjacentCoord(coords.y() < worldSize - 1, new Coords(coords.x(), coords.y() + 1));
        Optional<Coords> downAdjacent = getAdjacentCoord(coords.y() > 0, new Coords(coords.x(), coords.y() - 1));
        Optional<Coords> rightAdjacent = getAdjacentCoord(coords.x() < worldSize - 1, new Coords(coords.x() + 1, coords.y()));
        Optional<Coords> leftAdjacent = getAdjacentCoord(coords.x() > 0, new Coords(coords.x() - 1, coords.y()));

        List.of(upAdjacent, downAdjacent, rightAdjacent, leftAdjacent)
                .forEach(adjacentCoord -> adjacentCoord.ifPresent(adjacent::add));

        return adjacent;
    }

    private Optional<Coords> getAdjacentCoord(boolean condition, Coords coords) {
        boolean emptyResult = rTree.search(getSmallestRect(coords))
                .isEmpty()
                .toBlocking()
                .first();

        if (condition && emptyResult)
            return Optional.of(coords);
        return Optional.empty();
    }



    public abstract List<Coords> createAlgo();

    protected Coords getFirstVertex() {
        return getVertexNearest(startRect);
    }

    private Coords getVertexNearest(Rectangle rectangle) {

        int width = (int) rectangle.getWidth();
        int height = (int) rectangle.getHeight();
        int x = (int) rectangle.getX();
        int y = (int) rectangle.getY();

        int yUp = y + height + 1;
        int yDown = y - 1;
        int xRight = x + width + 1;
        int xLeft = x - 1;



        for (int tmpX = x; tmpX <= tmpX + width; tmpX++) {
            int tmpY = yUp;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                return new Coords(tmpX, tmpY);
        }

        for (int tmpX = x; tmpX <= tmpX + width; tmpX++) {
            int tmpY = yDown;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                return new Coords(tmpX, tmpY);
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xRight;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                return new Coords(tmpX, tmpY);
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xLeft;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                return new Coords(tmpX, tmpY);
        }

        return null;
    }

    protected boolean isMatrixPart(Coords coords) {
        boolean result = false;
        return rTree.search(getSmallestRect(coords), 1.0).forEach(entry -> {
            if (entry.value() == endRect) return true;
        });
        return result;
    }

    private RectangleFloat getSmallestRect(Coords coords){
        return (RectangleFloat) Geometries.rectangle(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1);
    }

    private boolean isEmpty(Coords coords){
       return rTree.search(getSmallestRect(coords))
                .isEmpty()
                .toBlocking()
                .first();
    }
}
