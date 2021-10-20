package com.github.alllef.battle_city.core.path_algorithm.lab3;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.AlgoType;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab2.AStarAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;

import java.util.List;
import java.util.Map;

public interface MiniMaxAlgo {

    default Map.Entry<Direction, Coords> getNearestCoord(Direction dir, Rectangle parRect) {
        Coords coords = null;
        switch (dir) {
            case LEFT -> coords = new Coords((int) parRect.getX() - 1, (int) parRect.getY());
            case RIGHT -> coords = new Coords((int) parRect.getX() + (int) parRect.getWidth() + 1, (int) parRect.getY());
            case UP -> coords = new Coords((int) parRect.getX(), (int) parRect.getY() + (int) parRect.getHeight() + 1);
            case DOWN -> coords = new Coords((int) parRect.getX(), (int) parRect.getY() - 1);
        }

        return Map.entry(dir, coords);
    }

    default Map.Entry<Direction, Rectangle> mapNearCoordsToRect(Direction dir, Coords coords, Rectangle parRect) {
        Rectangle rect = null;
        switch (dir) {
            case LEFT, DOWN -> rect = new Rectangle(coords.x(), coords.y(), parRect.getWidth(), parRect.getHeight());
            case RIGHT -> rect = new Rectangle(coords.x() - parRect.getWidth(), coords.y(), parRect.getWidth(), parRect.getHeight());
            case UP -> rect = new Rectangle(coords.x(), coords.y() - parRect.getHeight(), parRect.getWidth(), parRect.getHeight());
        }

        return Map.entry(dir, rect);
    }

    default int calcLeafFunc(Rectangle start, Rectangle end) {
        PathAlgo algo = new AStarAlgo(start, end, AlgoType.ASTAR_COORDS);
        List<Coords> result = algo.startAlgo();
        if (result.size() == 0)
            return Integer.MAX_VALUE;

        return result.size();
    }
}