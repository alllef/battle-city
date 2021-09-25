package com.github.alllef.battle_city.core.game_entities.tank;

import com.github.alllef.battle_city.core.game_entities.bullet.BulletFactory;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class EnemyTank extends SingleTank {

    public EnemyTank(BulletFactory bulletFactory, float x, float y) {
        super(SpriteParam.ENEMY_TANK.getTexturePath(), bulletFactory);
        this.getSprite().setPosition(x, y);
    }

    @Override
    public void shoot() {
        setDurationBetweenBullets(3 * prefs.getInteger("bullets_cooldown"));
        super.shoot();
    }
}
