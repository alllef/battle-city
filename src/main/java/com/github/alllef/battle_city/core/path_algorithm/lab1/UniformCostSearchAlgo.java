package com.github.alllef.battle_city.core.path_algorithm.lab1;

import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class UniformCostSearchAlgo extends PathAlgo {
    Map<Coords, Boolean> coordsBooleanMap = new HashMap<>();

    private record Node(Node parent, Coords child, int distance) {}
    PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::distance));

    public UniformCostSearchAlgo(GameEntity startEntity, GameEntity endEntity) {
        super(startEntity, endEntity);
    }

    @Override
    public List<Coords> createAlgo() {
        queue.add(new Node(null, getFirstVertex(), 0));
        if (queue.peek().child==null) return new ArrayList<>();
        return nextVertex(queue.peek());
    }

    public List<Coords> nextVertex(Node prevVertex) {

        getPossibleAdjacentVertices(prevVertex.child)
                .forEach(vertex -> {
                    if (!coordsBooleanMap.get(vertex)) {
                        coordsBooleanMap.put(vertex, true);
                        queue.add(new Node(prevVertex, vertex, prevVertex.distance + 1));
                    }

                });
        Node last = queue.poll();


        if (last == null)
            return new ArrayList<>();

        if (isMatrixPart(last.child))
            return getPath(last);

        nextVertex(last);
        return new ArrayList<>();
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
