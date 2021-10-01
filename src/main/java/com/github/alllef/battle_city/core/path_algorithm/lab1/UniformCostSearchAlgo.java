package com.github.alllef.battle_city.core.path_algorithm.lab1;

import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class UniformCostSearchAlgo extends PathAlgo {
    int[][] distanceMatrix = new int[worldSize][worldSize];
    Coords[][] parentMatrix = new Coords[worldSize][worldSize];

    PriorityQueue<Coords> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(coords -> distanceMatrix[coords.x()][coords.y()]));

    public UniformCostSearchAlgo(GameEntity startEntity, GameEntity endEntity) {
        super(startEntity, endEntity);
    }

    @Override
    public List<Coords> createAlgo() {
        Coords last = getFirstVertex();
        distanceMatrix[last.x()][last.y()] = 0;
        parentMatrix[last.x()][last.y()] = new Coords(-1, -1);
        climbedPeaksMatrix[last.x()][last.y()] = true;

        priorityQueue.add(last);

        while (last != null) {
            if (isMatrixPart(last))
                return getPath(last);

            nextVertex(last);
            priorityQueue.poll();
            last = priorityQueue.peek();
        }

        return new ArrayList<>();
    }

    public void nextVertex(Coords prevVertex) {

        getPossibleAdjacentVertices(prevVertex)
                .forEach(vertex -> {
                    parentMatrix[vertex.x()][vertex.y()] = prevVertex;
                    distanceMatrix[vertex.x()][vertex.y()] = distanceMatrix[prevVertex.x()][prevVertex.y()] + 1;
                    priorityQueue.add(vertex);
                });
    }

    private List<Coords> getPath(Coords lastVertex) {
        List<Coords> coords = new LinkedList<>();
        if (lastVertex == null) return coords;

        while (!parentMatrix[lastVertex.x()][lastVertex.y()].equals(new Coords(-1, -1))) {
            coords.add(lastVertex);
            lastVertex = parentMatrix[lastVertex.x()][lastVertex.y()];
        }

        return coords;
    }

}
