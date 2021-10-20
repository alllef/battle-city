package com.github.alllef.battle_city.core.game_entity.tank.enemy.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta.MiniMaxAlphaBetaAlgo;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.HashMap;
import java.util.Map;

public class ReflexEnemyTankManager extends EntityManager<ReflexEnemyTank> {
    private static ReflexEnemyTankManager enemyTankManager;

    public static ReflexEnemyTankManager getInstance() {
        if (enemyTankManager == null) {
            Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
            enemyTankManager = new ReflexEnemyTankManager(3,3,BulletFactory.getInstance());
        }

        return enemyTankManager;
    }

    PlayerTank player = PlayerTank.getInstance();
    RTreeMap rTreeMap = RTreeMap.getInstance();
    GdxToRTreeRectangleMapper mapper = GdxToRTreeRectangleMapper.ENTITY;

    Map<ReflexEnemyTank, Direction> dirMap = new HashMap<>();
    private final BulletFactory bulletFactory;
    private int execCount = 0;

    private ReflexEnemyTankManager(int randomTankNum, int playerTankNum, BulletFactory bulletFactory) {
        this.bulletFactory = bulletFactory;
        generateTanks(randomTankNum, playerTankNum);
    }

    private void generateTanks(int randomTankNum, int playerTankNum) {
        int worldSize = prefs.getInteger("world_size");
        for (int i = 0; i < randomTankNum; i++) {
            int x = (int) (Math.random() * worldSize * 0.95);
            int y = (int) (Math.random() * worldSize * 0.95);
            entityArr.add(new RandomReflexEnemyTank(rTreeMap, bulletFactory, x, y));
        }

        for (int i = 0; i < playerTankNum; i++) {
            int x = (int) (Math.random() * worldSize * 0.95);
            int y = (int) (Math.random() * worldSize * 0.95);
            entityArr.add(new PlayerReflexEnemyTank(rTreeMap, bulletFactory, x, y));
        }
    }

    public void ride() {
        if (execCount < 50) {
            execCount++;
            dirMap.forEach(ReflexEnemyTank::ride);
        }

        else {
            for (ReflexEnemyTank tank : entityArr) {
                if (tank instanceof PlayerReflexEnemyTank) {
                    MiniMaxAlphaBetaAlgo algo = new MiniMaxAlphaBetaAlgo(tank.getRect(), player.getRect(), tank.getDir());
                    dirMap.put(tank, algo.startAlgo(2));
                } else {
                    MiniMaxAlphaBetaAlgo algo = new MiniMaxAlphaBetaAlgo(tank.getRect(),mapper.convertToGdxRectangle(rTreeMap.getSmallestRect(rTreeMap.getRandomNonObstacleCoord())), tank.getDir());
                    dirMap.put(tank, algo.startAlgo(2));
                }
            }
            execCount = 0;
        }
    }

    public void shoot() {
        entityArr.forEach(EnemyTank::shoot);
    }
}
