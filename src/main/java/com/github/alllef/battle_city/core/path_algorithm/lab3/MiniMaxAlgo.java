package com.github.alllef.battle_city.core.path_algorithm.lab3;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta.AlphaBetaNode;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.enums.Direction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MiniMaxAlgo<T extends MiniMaxNode> {
    protected Rectangle start;
    protected Rectangle end;
    protected Direction dir;

    public MiniMaxAlgo(Rectangle start, Rectangle end, Direction dir) {
        this.start = start;
        this.end = end;
        this.dir = dir;
    }

    protected Map.Entry<Direction, Coords> getNearestCoord(Direction dir, Rectangle parRect) {
        Coords coords = null;
        switch (dir) {
            case LEFT -> coords = new Coords((int) parRect.getX() - 1, (int) parRect.getY());
            case RIGHT -> coords = new Coords((int) parRect.getX() + (int) parRect.getWidth() + 1, (int) parRect.getY());
            case UP -> coords = new Coords((int) parRect.getX(), (int) parRect.getY() + (int) parRect.getHeight() + 1);
            case DOWN -> coords = new Coords((int) parRect.getX(), (int) parRect.getY() - 1);
        }

        return Map.entry(dir, coords);
    }

    protected Map.Entry<Direction, Rectangle> mapNearCoordsToRect(Direction dir, Coords coords, Rectangle parRect) {
        Rectangle rect = null;
        switch (dir) {
            case LEFT, DOWN -> rect = new Rectangle(coords.x(), coords.y(), parRect.getWidth(), parRect.getHeight());
            case RIGHT -> rect = new Rectangle(coords.x() - parRect.getWidth(), coords.y(), parRect.getWidth(), parRect.getHeight());
            case UP -> rect = new Rectangle(coords.x(), coords.y() - parRect.getHeight(), parRect.getWidth(), parRect.getHeight());
        }

        return Map.entry(dir, rect);
    }

    protected int calcLeafFunc(Rectangle start, Rectangle end) {
        return (int) new Coords((int) start.getX(), (int) start.getY()).calcCoordDist(new Coords((int) end.getX(), (int) end.getY()));
    }

    protected Optional<T> getUnusedChild(T node, List<T> children) {
        Optional<T> child = Optional.empty();

        for (T tmpChild : children) {
            if (!tmpChild.isTraversed()) {
                tmpChild.setTraversed(true);
                return Optional.of(tmpChild);
            }
        }
        return child;
    }
}
