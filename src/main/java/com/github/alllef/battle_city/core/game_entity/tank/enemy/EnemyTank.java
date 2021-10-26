package com.github.alllef.battle_city.core.game_entity.tank.enemy;

import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;

public class EnemyTank extends SingleTank {

    public EnemyTank(BulletFactory bulletFactory, float x, float y, Preferences prefs) {
        super(x, y, SpriteParam.ENEMY_TANK, bulletFactory, prefs);
    }

    @Override
    public void shoot() {
        setDurationBetweenBullets(3 * prefs.getInteger("bullets_cooldown"));
        super.shoot();
    }

}
