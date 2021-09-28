package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.world.MatrixMap;

import java.util.ArrayList;
import java.util.List;

public abstract class PathAlgo {
    MatrixMap matrix = MatrixMap.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();

    protected List<Coords> getAdjacentVertices(Coords coords) {
        boolean[][] entityMatr = matrix.getEntityMatrix();
        List<Coords> adjacent = new ArrayList<>();
        if (coords.x() > 0)
            adjacent.add(new Coords(coords.x() - 1, coords.y()));
        if (coords.y() > 0)
            adjacent.add(new Coords(coords.x(), coords.y() - 1));
        if (coords.x() < entityMatr.length - 1)
            adjacent.add(new Coords(coords.x() + 1, coords.y()));
        if (coords.y() < entityMatr.length - 1)
            adjacent.add(new Coords(coords.x(), coords.y() + 1));
        return adjacent;
    }

    public abstract List<Coords> createAlgo();

    protected boolean isMatrixPart(Rectangle rect, Coords coords) {

        return false;
    }
}
