package com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos;

import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class UCSAlgo extends BFSAlgo {
    protected int[][] distanceMatrix = new int[worldSize][worldSize];

    public UCSAlgo(GameEntity startEntity, GameEntity endEntity) {
        super(startEntity, endEntity);
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