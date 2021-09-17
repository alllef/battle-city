package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.util.Direction;

public class EnemyTankManager {
    private Array<EnemyTank> enemyTanks;
    private Array<Long> millis;

    public EnemyTankManager() {
        System.out.println("wtf");
        enemyTanks = new Array<>();
        enemyTanks.add(new EnemyTank());
        millis = new Array<>();
        for (EnemyTank tank : enemyTanks) {
            millis.add(TimeUtils.millis());
        }
    }

    public void ride() {
        for (int i = 0; i < enemyTanks.size; i++) {
            if (TimeUtils.millis() - millis.get(i) > 1000) {
                enemyTanks.get(i).ride(Direction.UP);
            }
        }
    }

    public Array<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

}
