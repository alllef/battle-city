package com.github.alllef.battle_city.core.tank;

import com.github.alllef.battle_city.core.bullet.BulletFactory;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class EnemyTank extends SingleTank {

    public EnemyTank(BulletFactory bulletFactory, float x, float y) {
        super(SpriteParam.ENEMY_TANK.getTexturePath(), bulletFactory);
        this.getTankSprite().setPosition(x, y);
    }

    @Override
    public void shoot() {
        setDurationBetweenBullets(3 * prefs.getInteger("bullets_cooldown"));
        super.shoot();
    }
}
