package com.github.alllef.battle_city.core.path_algorithm.lab1.algos.bfs_like_algos;

import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.LinkedList;
import java.util.List;

public abstract class BFSLikeAlgo extends PathAlgo {
    Coords[][] parentMatrix = new Coords[worldSize][worldSize];

    public BFSLikeAlgo(GameEntity startEntity, GameEntity endEntity) {
        super(startEntity, endEntity);
    }

    public List<Coords> getPath(Coords lastVertex) {
        List<Coords> coords = new LinkedList<>();
        if (lastVertex == null) return coords;

        while (!parentMatrix[lastVertex.x()][lastVertex.y()].equals(new Coords(-1, -1))) {
            coords.add(lastVertex);
            lastVertex = parentMatrix[lastVertex.x()][lastVertex.y()];
        }

        return coords;
    }

}
