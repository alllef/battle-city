package com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.Comparator;
import java.util.PriorityQueue;

public class UCSAlgo extends BFSAlgo {
    protected int[][] distanceMatrix = new int[worldSize][worldSize];

    public UCSAlgo(Rectangle startRect, Rectangle endRect) {
        super(startRect, endRect);
        collection = new PriorityQueue<>(Comparator.comparingInt(coords -> distanceMatrix[coords.x()][coords.y()]));
    }

    @Override
    protected Coords getFirstVertex() {
        Coords coords = super.getFirstVertex();
        if (coords != null) distanceMatrix[coords.x()][coords.y()] = 0;
        return coords;
    }

    @Override
    protected void handleAddedVertex(Coords parent, Coords child) {
        distanceMatrix[child.x()][child.y()] = distanceMatrix[parent.x()][parent.y()] + 1;
        super.handleAddedVertex(parent, child);
    }
}
