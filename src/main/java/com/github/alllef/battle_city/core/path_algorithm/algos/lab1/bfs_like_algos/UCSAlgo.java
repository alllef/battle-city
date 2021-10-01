package com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos;

import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class UCSAlgo extends BFSLikeAlgo {
    protected int[][] distanceMatrix = new int[worldSize][worldSize];
    protected PriorityQueue<Coords> priorityQueue;

    public UCSAlgo(GameEntity startEntity, GameEntity endEntity) {
        super(startEntity, endEntity);
        priorityQueue = new PriorityQueue<>(Comparator.comparingInt(coords -> distanceMatrix[coords.x()][coords.y()]));
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

}
