package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.path_algorithm.lab1.BFSAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.ArrayList;
import java.util.List;

public class TankManipulation {
    PlayerTank playerTank = PlayerTank.getInstance();
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    PathAlgo pathAlgo;

    List<Coords> pathsToDraw = new ArrayList<>();

    public void setPathAlgo(PathAlgo pathAlgo) {
        this.pathAlgo = pathAlgo;
    }

    public void update() {
        long seconds = TimeUtils.millis();
        enemyTankManager.getEnemyTanks().forEach(enemyTank -> {

                    BFSAlgo bfsAlgo = new BFSAlgo(playerTank.getSprite().getBoundingRectangle(), enemyTank.getSprite().getBoundingRectangle());
                    bfsAlgo.createAlgo();
                    // pathsToDraw.addAll(pathAlgo.createAlgo());
                }
        );
        System.out.println(TimeUtils.millis()-seconds);
    }

}
