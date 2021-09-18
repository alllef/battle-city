package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;

public class EnemyTankManager implements Drawable {
    private Array<EnemyTank> enemyTanks;
    private Array<Long> millis;

    public EnemyTankManager() {
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
                Direction direction = Direction.values()[(int) (Math.random() * Direction.values().length)];
                enemyTanks.get(i).ride(direction);
                millis.set(i, TimeUtils.millis());
            }
        }
    }

    public Array<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        this.getEnemyTanks()
                .forEach(
                        enemyTank -> {
                            Sprite sprite = enemyTank.getTankSprite();
                            sprite.setPosition(0, 0);
                            sprite.draw(spriteBatch);
                        }
                );
    }

}
