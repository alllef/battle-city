package com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos;

import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class BFSAlgo extends BFSLikeAlgo {

    Queue<Coords> coordsQueue = new LinkedList<>();

    public BFSAlgo(GameEntity startEntity, GameEntity endEntity) {
        super(startEntity, endEntity);
    }

    public List<Coords> createAlgo() {
        Coords last = getFirstVertex();

        parentMatrix[last.x()][last.y()] = new Coords(-1, -1);
        climbedPeaksMatrix[last.x()][last.y()] = true;
        coordsQueue.add(last);

        while (last != null) {
            nextVertex(last);

            if (isMatrixPart(last))
                return getPath(last);

            coordsQueue.poll();
            last = coordsQueue.peek();
        }

        return new ArrayList<>();
    }


    public void nextVertex(Coords vertex) {
        List<Coords> adjacentVertices = getPossibleAdjacentVertices(vertex);
        for (Coords adjacentVertex : adjacentVertices) {
                parentMatrix[adjacentVertex.x()][adjacentVertex.y()] = vertex;
                coordsQueue.add(adjacentVertex);
            }
        }

}
