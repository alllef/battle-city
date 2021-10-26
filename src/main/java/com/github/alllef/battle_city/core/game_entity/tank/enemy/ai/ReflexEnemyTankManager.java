package com.github.alllef.battle_city.core.game_entity.tank.enemy.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.ExpectiMaxAlgo;
import com.github.alllef.battle_city.core.util.RectUtils;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.RTreeMap;
import com.github.alllef.battle_city.core.world.stats.ScoreManipulation;

public class ReflexEnemyTankManager extends EntityManager<ReflexEnemyTank> {

    PlayerTankManager player;
    RTreeMap rTreeMap;
    GdxToRTreeRectangleMapper mapper = GdxToRTreeRectangleMapper.ENTITY;
    protected final ScoreManipulation scoreManipulation = ScoreManipulation.INSTANCE;
    int counter = 0;

    private final BulletFactory bulletFactory;

    public ReflexEnemyTankManager(int randomTankNum, int playerTankNum, BulletFactory bulletFactory,PlayerTankManager player,RTreeMap rTreeMap) {
        this.bulletFactory = bulletFactory;
        this.player=player;
        this.rTreeMap=rTreeMap;
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
        Rectangle endRect;
        if (counter < 10)
            counter++;
        else {
            counter = 0;
            for (ReflexEnemyTank tank : entityArr) {
                if (tank instanceof PlayerReflexEnemyTank)
                    endRect = player.getSprite().getBoundingRectangle();
                else
                    endRect = mapper.convertToGdxRectangle(RectUtils.getSmallestRect(rTreeMap.getRandomNonObstacleCoord(SpriteParam.ENEMY_TANK)));

                ExpectiMaxAlgo algo = new ExpectiMaxAlgo(tank.getRect(), endRect, tank.getDir());
                tank.ride(algo.startAlgo(5));
            }
        }
        getEntities().forEach(enemyTank -> enemyTank.ride(enemyTank.getDir()));
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
        System.out.println(rTreeMap.getRtreeSize()+ " Size in ReflexEnemyTankManager");
        this.shoot();
        super.update();
    }

}
