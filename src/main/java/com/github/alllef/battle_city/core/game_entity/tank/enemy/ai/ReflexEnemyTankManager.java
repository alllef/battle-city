package com.github.alllef.battle_city.core.game_entity.tank.enemy.ai;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;
import com.github.alllef.battle_city.core.path_algorithm.lab3.MiniMaxAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.ExpectiMaxAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta.MiniMaxAlphaBetaAlgo;
import com.github.alllef.battle_city.core.util.RectUtils;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.RTreeMap;
import com.github.alllef.battle_city.core.world.stats.ScoreManipulation;

public class ReflexEnemyTankManager extends EntityManager<ReflexEnemyTank> {
    private final PlayerTankManager player;
    private final RTreeMap rTreeMap;
    private final GdxToRTreeRectangleMapper mapper = GdxToRTreeRectangleMapper.ENTITY;
    protected final ScoreManipulation scoreManipulation;
    private int updatePathCounter = 0;

    private final BulletFactory bulletFactory;

    public ReflexEnemyTankManager(int randomTankNum, int playerTankNum, BulletFactory bulletFactory, PlayerTankManager player, RTreeMap rTreeMap, ScoreManipulation scoreManipulation, Preferences prefs) {
        super(prefs);
        this.bulletFactory = bulletFactory;
        this.player = player;
        this.rTreeMap = rTreeMap;
        this.scoreManipulation = scoreManipulation;
        generateTanks(randomTankNum, playerTankNum);
    }

    private void generateTanks(int randomTankNum, int playerTankNum) {
        int worldSize = prefs.getInteger("world_size");
        for (int i = 0; i < randomTankNum; i++) {
            int x = (int) (Math.random() * worldSize * 0.95);
            int y = (int) (Math.random() * worldSize * 0.95);
            entityArr.add(new RandomReflexEnemyTank(rTreeMap, bulletFactory, x, y, prefs));
        }

        for (int i = 0; i < playerTankNum; i++) {
            int x = (int) (Math.random() * worldSize * 0.95);
            int y = (int) (Math.random() * worldSize * 0.95);
            entityArr.add(new PlayerReflexEnemyTank(rTreeMap, bulletFactory, x, y, prefs));
        }
    }

    public void ride() {
        if (updatePathCounter < prefs.getInteger("update_minimax_path_frame"))
            updatePathCounter++;
        else {
            updatePathCounter = 0;
            changeRideDir();
        }

        getEntities().forEach(enemyTank -> enemyTank.ride(enemyTank.getDir()));
    }

    private void changeRideDir() {
        Rectangle endRect;

        for (ReflexEnemyTank tank : entityArr) {
            if (tank instanceof PlayerReflexEnemyTank)
                endRect = player.getSprite().getBoundingRectangle();
            else
                endRect = mapper.convertToGdxRectangle(RectUtils.getSmallestRect(rTreeMap.getRandomNonObstacleCoord(SpriteParam.ENEMY_TANK)));

            var optionalDir = tank.getTanksOnAnyParallel();
            if (optionalDir.isPresent()) {
                tank.ride(optionalDir.get());
                System.out.println("Way");
            }
            else
                tank.ride(getDirectionByAlgo(tank, endRect));
        }
    }

    private Direction getDirectionByAlgo(ReflexEnemyTank tank, Rectangle endRect) {
        MiniMaxAlgo algo = null;

        switch (prefs.getString("minimax_algo_type")) {
            case "expectimax" -> algo = new ExpectiMaxAlgo(this.rTreeMap, tank.getRect(), endRect, tank.getDir());
            case "alphabeta" -> algo = new MiniMaxAlphaBetaAlgo(this.rTreeMap, tank.getRect(), endRect, tank.getDir());
        }

        return algo.startAlgo(prefs.getInteger("minimax_algo_depth"));
    }

    public void shoot() {
        entityArr.forEach(ReflexEnemyTank::shoot);
    }

    public void bulletShoot(ReflexEnemyTank enemyTank) {
        getEntities().removeValue(enemyTank, true);
        scoreManipulation.tankKilled();
    }

    @Override
    public void update() {
        this.ride();
        this.shoot();
        super.update();
    }

}
