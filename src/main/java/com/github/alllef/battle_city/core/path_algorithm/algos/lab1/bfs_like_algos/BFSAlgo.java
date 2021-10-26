package com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.*;

public class BFSAlgo extends PathAlgo<Queue<Coords>> {
    protected Coords[][] parentMatrix = new Coords[worldSize][worldSize];

    public BFSAlgo(RTreeMap rTreeMap, Rectangle startRect, Rectangle endRect) {
        super(rTreeMap, startRect, endRect);
        collection = new LinkedList<>();
    }

    @Override
    public List<Coords> startAlgo() {
        Coords last = getFirstVertex();
        collection.add(last);

        while (last != null) {
            nextVertex(last);

            if (isMatrixPart(last))
                return getPath(last);

            collection.poll();
            last = collection.peek();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Coords> getPath(Coords lastVertex) {
        List<Coords> coords = new LinkedList<>();
        if (lastVertex == null) return coords;

        while (!parentMatrix[lastVertex.x()][lastVertex.y()].equals(new Coords(-1, -1))) {
            coords.add(lastVertex);
            lastVertex = parentMatrix[lastVertex.x()][lastVertex.y()];
        }

        return coords;
    }

    @Override
    protected Coords getFirstVertex() {
        Coords coords = super.getFirstVertex();
        if (coords != null) {
            parentMatrix[coords.x()][coords.y()] = new Coords(-1, -1);
            climbedPeaksMatrix[coords.x()][coords.y()] = true;
        }

        return coords;
    }

    protected void handleAddedVertex(Coords parent, Coords child) {
        parentMatrix[child.x()][child.y()] = parent;
        collection.add(child);
    }

    protected void nextVertex(Coords vertex) {
        getPossibleAdjacentVertices(vertex)
                .forEach(adjacentVertex -> handleAddedVertex(vertex, adjacentVertex));
    }

}
