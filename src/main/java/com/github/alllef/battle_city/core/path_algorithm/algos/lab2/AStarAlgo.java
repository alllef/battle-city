package com.github.alllef.battle_city.core.path_algorithm.algos.lab2;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.AlgoType;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos.UCSAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class AStarAlgo extends UCSAlgo {
    AlgoType algoType;
    Map<Coords, Boolean> hasCoinsMap = new HashMap<>();

    public AStarAlgo(Rectangle startRect, Rectangle endRect, AlgoType algoType) {
        super(startRect, endRect);
        this.algoType = algoType;
        collection = new PriorityQueue<>(Comparator.comparing(this::calculateFunction));
    }

    private float calculateHeuristics(Coords first, Coords second) {
        float res = 0.0f;
        switch (algoType) {
            case ASTAR_COORDS -> res = first.calcCoordDist(second);
            case ASTAR_MANHATTAN -> res = first.calcManhattanDist(second);
            case ASTAR_GREEDY -> res = 0;
        }
        return res;
    }

    public float calculateFunction(Coords coords) {
        Coords endCoords = new Coords((int) endRect.getX(), (int) endRect.getY());
        Coords parent = parentMatrix[coords.x()][coords.y()];
        float functionResult = calculateHeuristics(coords, endCoords);

        if (!parent.equals(new Coords(-1, -1))) {
            float coordDist = prefs.getFloat("coord_distance");
            if (hasCoins(coords))
                coordDist = coordDist / 2;

            functionResult += distanceMatrix[parent.x()][parent.y()] + coordDist;
        }

        return functionResult;
    }

    private boolean hasCoins(Coords coords) {
        Optional<Boolean> hasCoins = Optional.ofNullable(hasCoinsMap.get(coords));
        if(hasCoins.isPresent())
            return hasCoins.get();
        hasCoinsMap.put(coords,rTreeMap.hasCoins(coords));

        return hasCoinsMap.get(coords);
    }
}
