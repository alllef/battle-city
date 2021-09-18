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
    private Map<EnemyTank, Step> stepsNum;

    public EnemyTankManager() {
        enemyTanks = new Array<>();
        stepsNum = new HashMap<>();
        enemyTanks.add(new EnemyTank());
    }

    public void ride() {
        for (int i = 0; i < enemyTanks.size; i++) {
            EnemyTank tmpTank = enemyTanks.get(i);
            Direction dir = tmpTank.getDir();

            if (stepsNum.get(tmpTank) == null || stepsNum.get(tmpTank).steps() == 0) {
                dir = Direction.values()[new Random().nextInt(4)];
                tmpTank.ride(dir);
                stepsNum.put(tmpTank, new Step(0.0f, 20));
            }

            Step tmpStep = stepsNum.get(tmpTank);
            float timePassed = tmpStep.timePassed() + Gdx.graphics.getDeltaTime();

            stepsNum.put(tmpTank, new Step(timePassed, tmpStep.steps()));

            System.out.println(tmpStep);

            if (timePassed >= 0.1f) {
                tmpTank.ride(dir);
                stepsNum.put(tmpTank,new Step(0.0f,tmpStep.steps()-1));
            }


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

    private static record Step(float timePassed, int steps) {
    }
}
