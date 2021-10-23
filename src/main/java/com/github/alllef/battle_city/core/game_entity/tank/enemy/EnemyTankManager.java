package com.github.alllef.battle_city.core.game_entity.tank.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.world.score.ScoreManipulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EnemyTankManager extends EntityManager<EnemyTank> implements Drawable {
    private static EnemyTankManager enemyTankManager;
    protected final ScoreManipulation scoreManipulation = ScoreManipulation.INSTANCE;

    public static EnemyTankManager getInstance() {
        if (enemyTankManager == null) {
            Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
            enemyTankManager = new EnemyTankManager(prefs.getInteger("enemy_tanks_number"), BulletFactory.getInstance());
        }
        return enemyTankManager;
    }

    private Map<EnemyTank, Integer> stepsNum;
    private final BulletFactory bulletFactory;

    private EnemyTankManager(int tankNumber, BulletFactory bulletFactory) {
        this.bulletFactory = bulletFactory;
        stepsNum = new HashMap<>();
        generateTanks(tankNumber);
    }

    private void generateTanks(int tankNumber) {
        int worldSize = prefs.getInteger("world_size");
        for (int i = 0; i < tankNumber; i++) {
            int x = (int) (Math.random() * worldSize * 0.95);
            int y = (int) (Math.random() * worldSize * 0.95);
            entityArr.add(new EnemyTank(bulletFactory, x, y));
        }
    }

    public void ride() {
        for (int i = 0; i < entityArr.size; i++) {
            EnemyTank tmpTank = entityArr.get(i);
            Direction dir = tmpTank.getDir();

            if (stepsNum.get(tmpTank) == null || stepsNum.get(tmpTank) <= 0) {
                dir = Direction.values()[new Random().nextInt(Direction.values().length)];
                tmpTank.ride(dir);
                stepsNum.put(tmpTank, new Random().nextInt(prefs.getInteger("max_ride_distance")));
            }

            tmpTank.ride(dir);
            stepsNum.put(tmpTank, stepsNum.get(tmpTank) - 1);
        }
        //enemyTanks.forEach(tank -> tank.ride(tank.getDir()));
    }

    public void shoot() {
        entityArr.forEach(EnemyTank::shoot);
    }

    public void bulletShoot(EnemyTank enemyTank) {
        getEntities().removeValue(enemyTank, false);
        scoreManipulation.tankKilled();
    }

    @Override
    public void update() {
        this.ride();
        this.shoot();
        super.update();
    }
}
