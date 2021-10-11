package com.github.alllef.battle_city.core.path_algorithm.algos.lab2;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos.UCSAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.Comparator;
import java.util.PriorityQueue;

public class AStarAlgo extends UCSAlgo {
    public AStarAlgo(Rectangle startRect, Rectangle endRect) {
        super(startRect, endRect);
        collection = new PriorityQueue<>(Comparator.comparing(this::calculateFunction));
    }

    private float calculateHeuristics(Coords first, Coords second) {
        return first.calcCoordDist(second);
    }

    public float calculateFunction(Coords coords) {
        Coords endCoords = new Coords((int) endRect.getX(), (int) endRect.getY());
        Coords parent = parentMatrix[coords.x()][coords.y()];
        float functionResult = calculateHeuristics(coords, endCoords);

        if (!parent.equals(new Coords(-1, -1)))
            functionResult += distanceMatrix[parent.x()][parent.y()] + 1;

        return functionResult;
    }

}
