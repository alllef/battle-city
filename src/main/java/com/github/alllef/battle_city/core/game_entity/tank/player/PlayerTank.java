package com.github.alllef.battle_city.core.game_entity.tank.player;

import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;

public class PlayerTank extends SingleTank {

    protected PlayerTank(BulletFactory bulletFactory, Preferences prefs) {
        super(0,0,SpriteParam.PLAYER_TANK, bulletFactory,prefs);
        setDurationBetweenBullets(prefs.getInteger("bullets_cooldown"));
    }

}
