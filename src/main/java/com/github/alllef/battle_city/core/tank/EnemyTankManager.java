package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EnemyTankManager implements Drawable {
    private Array<EnemyTank> enemyTanks;
    private Map<EnemyTank, Integer> stepsNum;

    public EnemyTankManager(int tankNumber) {
        enemyTanks = new Array<>();
        stepsNum = new HashMap<>();
        generateTanks(tankNumber);
    }

    private void generateTanks(int tankNumber) {

        for (int i = 0; i < tankNumber; i++) {
            int x = (int) (Math.random() * 80);
            int y = (int) (Math.random() * 80);
            enemyTanks.add(new EnemyTank(x, y));
        }
    }

    public void ride() {
        for (int i = 0; i < enemyTanks.size; i++) {
            EnemyTank tmpTank = enemyTanks.get(i);
            Direction dir = tmpTank.getDir();

            if (stepsNum.get(tmpTank) == null || stepsNum.get(tmpTank) <= 0) {
                dir = Direction.values()[new Random().nextInt(Direction.values().length)];
                tmpTank.ride(dir);
                stepsNum.put(tmpTank, new Random().nextInt(40));
            }

            tmpTank.ride(dir);
            System.out.println(stepsNum.get(tmpTank));
            stepsNum.put(tmpTank, stepsNum.get(tmpTank) - 1);
        }
    }

    public Array<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        this.getEnemyTanks()
                .forEach(enemyTank ->
                        enemyTank.getTankSprite().draw(spriteBatch));
    }

}
