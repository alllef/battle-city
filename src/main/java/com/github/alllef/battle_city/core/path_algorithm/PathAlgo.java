package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class PathAlgo<T extends Collection<Coords>> {
    protected RTreeMap rTreeMap = RTreeMap.getInstance();
    protected final int worldSize;
    protected boolean[][] climbedPeaksMatrix;
    protected Rectangle startRect;
    protected Rectangle endRect;
    protected T collection;
    protected Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    public PathAlgo(Rectangle startRect, Rectangle endRect) {

        worldSize = prefs.getInteger("world_size");
        this.startRect = startRect;
        this.endRect = endRect;
        climbedPeaksMatrix = new boolean[worldSize][worldSize];
    }

    public abstract List<Coords> startAlgo();

    protected abstract List<Coords> getPath(Coords start);


    protected List<Coords> getPossibleAdjacentVertices(Coords coords) {
        List<Coords> adjacent = new LinkedList<>();

        Optional<Coords> upAdjacent = getAdjacentCoord(coords.y() < worldSize - 1, new Coords(coords.x(), coords.y() + 1));
        Optional<Coords> downAdjacent = getAdjacentCoord(coords.y() > 0, new Coords(coords.x(), coords.y() - 1));
        Optional<Coords> rightAdjacent = getAdjacentCoord(coords.x() < worldSize - 1, new Coords(coords.x() + 1, coords.y()));
        Optional<Coords> leftAdjacent = getAdjacentCoord(coords.x() > 0, new Coords(coords.x() - 1, coords.y()));

        List.of(upAdjacent, downAdjacent, rightAdjacent, leftAdjacent)
                .forEach(adjacentCoord -> adjacentCoord.ifPresent(adjacent::add));

        return adjacent;
    }


    protected Optional<Coords> getAdjacentCoord(boolean condition, Coords coords) {
        Optional<Coords> tmpCoords = Optional.empty();
        if (condition && !climbedPeaksMatrix[coords.x()][coords.y()]) {
            if (rTreeMap.isEmpty(coords))
                tmpCoords = Optional.of(coords);

            climbedPeaksMatrix[coords.x()][coords.y()] = true;
        }
        return tmpCoords;
    }

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
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && rTreeMap.isEmpty(new Coords(tmpX, tmpY)))
                return new Coords(tmpX, tmpY);
        }

        for (int tmpX = x; tmpX <= tmpX + width; tmpX++) {
            int tmpY = yDown;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && rTreeMap.isEmpty(new Coords(tmpX, tmpY)))
                return new Coords(tmpX, tmpY);
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xRight;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && rTreeMap.isEmpty(new Coords(tmpX, tmpY)))
                return new Coords(tmpX, tmpY);
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xLeft;
            if (tmpY < worldSize && tmpX < worldSize && tmpY >= 0 && tmpX >= 0 && rTreeMap.isEmpty(new Coords(tmpX, tmpY)))
                return new Coords(tmpX, tmpY);
        }

        return null;
    }

    protected boolean isMatrixPart(Coords coords) {
        Rectangle nearRect = new Rectangle(coords.x() - 1, coords.y() - 1, 3, 3);
        return nearRect.overlaps(endRect);
    }

}
