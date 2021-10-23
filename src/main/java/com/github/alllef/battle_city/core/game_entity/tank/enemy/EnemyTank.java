package com.github.alllef.battle_city.core.game_entity.tank.enemy;

import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;

public class EnemyTank extends SingleTank {

    public EnemyTank(BulletFactory bulletFactory, float x, float y) {
        super(x,y,SpriteParam.ENEMY_TANK, bulletFactory);
    }

    @Override
    public void shoot() {
        setDurationBetweenBullets(3 * prefs.getInteger("bullets_cooldown"));
        super.shoot();
    }

}
