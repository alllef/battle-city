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
    protected boolean[][] climbedPeaksMatrix;
    private GameEntity startEntity;
    private GameEntity endEntity;

    public PathAlgo(GameEntity startEntity, GameEntity endEntity) {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        worldSize = prefs.getInteger("world_size");
        this.startEntity = startEntity;
        this.endEntity = endEntity;
        climbedPeaksMatrix = new boolean[worldSize][worldSize];
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
        Optional<Coords> tmpCoords = Optional.empty();
        if (condition && !climbedPeaksMatrix[coords.x()][coords.y()]) {
            if (isEmpty(coords))
                tmpCoords = Optional.of(coords);

            climbedPeaksMatrix[coords.x()][coords.y()] = true;
        }
        return tmpCoords;
    }


    public abstract List<Coords> createAlgo();

    protected Coords getFirstVertex() {
        return getVertexNearest(startEntity.getSprite().getBoundingRectangle());
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
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && isEmpty(new Coords(tmpX, tmpY)))
                return new Coords(tmpX, tmpY);
        }

        for (int tmpX = x; tmpX <= tmpX + width; tmpX++) {
            int tmpY = yDown;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && isEmpty(new Coords(tmpX, tmpY)))
                return new Coords(tmpX, tmpY);
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xRight;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && isEmpty(new Coords(tmpX, tmpY)))
                return new Coords(tmpX, tmpY);
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xLeft;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && isEmpty(new Coords(tmpX, tmpY)))
                return new Coords(tmpX, tmpY);
        }

        return null;
    }

    protected boolean isMatrixPart(Coords coords) {
        Rectangle nearRect = new Rectangle(coords.x() - 1, coords.y() - 1, 3, 3);
        return nearRect.overlaps(endEntity.getSprite().getBoundingRectangle());
    }

    private RectangleFloat getSmallestRect(Coords coords) {
        return (RectangleFloat) Geometries.rectangle(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1);
    }

    private boolean isEmpty(Coords coords) {
        return rTree.search(getSmallestRect(coords))
                .isEmpty()
                .toBlocking()
                .first();
    }
}
