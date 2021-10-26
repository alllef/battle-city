package com.github.alllef.battle_city.core.game_entity.tank.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.interfaces.Drawable;
import com.github.alllef.battle_city.core.world.stats.ScoreManipulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EnemyTankManager extends EntityManager<EnemyTank> implements Drawable {
    protected final ScoreManipulation scoreManipulation = ScoreManipulation.INSTANCE;


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
            Random rand = new Random();

            if (stepsNum.get(tmpTank) == null || stepsNum.get(tmpTank) <= 0) {
                dir = Direction.values()[rand.nextInt(Direction.values().length)];
                tmpTank.ride(dir);
                stepsNum.put(tmpTank, rand.nextInt(prefs.getInteger("max_ride_distance")));
            }
            Integer result = stepsNum.get(tmpTank);

            tmpTank.ride(dir);
            stepsNum.put(tmpTank, result - 1);
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
