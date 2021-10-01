package com.github.alllef.battle_city.core.path_algorithm.lab1;

import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class UniformCostSearchAlgo extends PathAlgo {

    private record Node(Node parent, Coords child, int distance) {
    }

    PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::distance));

    public UniformCostSearchAlgo(GameEntity startEntity, GameEntity endEntity) {
        super(startEntity, endEntity);
    }

    @Override
    public List<Coords> createAlgo() {
        Node lastVertex = new Node(null, getFirstVertex(), 0);
        priorityQueue.add(lastVertex);

        while (lastVertex != null) {

            if (isMatrixPart(lastVertex.child()))
                return getPath(lastVertex);

            nextVertex(lastVertex);
            priorityQueue.poll();
            lastVertex = priorityQueue.peek();

        }
        return new ArrayList<>();
    }

    public void nextVertex(Node prevVertex) {

        getPossibleAdjacentVertices(prevVertex.child)
                .forEach(vertex -> priorityQueue.add(new Node(prevVertex, vertex, prevVertex.distance() + 1)));

    }

    private List<Coords> getPath(Node lastVertex) {
        List<Coords> coords = new LinkedList<>();
        if (lastVertex.child == null) return coords;

        while (lastVertex.parent != null) {
            coords.add(lastVertex.child());
            lastVertex = lastVertex.parent();
        }
        return coords;
    }

}
