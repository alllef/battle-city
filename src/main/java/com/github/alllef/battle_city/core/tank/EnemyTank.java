package com.github.alllef.battle_city.core.tank;

import com.github.alllef.battle_city.core.util.SpriteParam;

public class EnemyTank extends SingleTank {

    public EnemyTank(float x, float y) {
        super(SpriteParam.ENEMY_TANK.getTexturePath());
        this.getTankSprite().setPosition(x, y);
    }

    @Override
    public void shoot() {
        setDurationBetweenBullets(3 * prefs.getInteger("bullets_cooldown"));
        super.shoot();
    }
}
