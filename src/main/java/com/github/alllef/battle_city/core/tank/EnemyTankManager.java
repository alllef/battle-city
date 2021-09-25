package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.bullet.BulletFactory;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EnemyTankManager implements Drawable {
    private Array<EnemyTank> enemyTanks;
    private Map<EnemyTank, Integer> stepsNum;
    private final BulletFactory bulletFactory;

    public EnemyTankManager(int tankNumber, BulletFactory bulletFactory) {
        this.bulletFactory = bulletFactory;
        enemyTanks = new Array<>();
        stepsNum = new HashMap<>();
        generateTanks(tankNumber);
    }

    private void generateTanks(int tankNumber) {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        for (int i = 0; i < tankNumber; i++) {
            int x = (int) (Math.random() * worldSize * 0.95);
            int y = (int) (Math.random() * worldSize * 0.95);
            enemyTanks.add(new EnemyTank(bulletFactory,x, y));
        }
    }

    public void ride() {
        for (int i = 0; i < enemyTanks.size; i++) {
            EnemyTank tmpTank = enemyTanks.get(i);
            Direction dir = tmpTank.getDir();

            if (stepsNum.get(tmpTank) == null || stepsNum.get(tmpTank) <= 0) {
                dir = Direction.values()[new Random().nextInt(Direction.values().length)];
                tmpTank.ride(dir);

                Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
                stepsNum.put(tmpTank, new Random().nextInt(prefs.getInteger("max_ride_distance")));
            }

            tmpTank.ride(dir);
            stepsNum.put(tmpTank, stepsNum.get(tmpTank) - 1);
        }
    }

    public Array<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

    public void shoot() {
        enemyTanks.forEach(EnemyTank::shoot);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        this.getEnemyTanks()
                .forEach(enemyTank ->
                        enemyTank.getTankSprite().draw(spriteBatch));
    }

}
