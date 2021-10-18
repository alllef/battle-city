package com.github.alllef.battle_city.core.game_entity.tank.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class PlayerTank extends SingleTank implements Drawable {
    private static final PlayerTank playerTank = new PlayerTank(BulletFactory.getInstance());

    public static PlayerTank getInstance() {
        return playerTank;
    }

    private boolean isRideLooping = false;

    protected PlayerTank(BulletFactory bulletFactory) {
        super(SpriteParam.PLAYER_TANK.getTexturePath(), bulletFactory);
        setDurationBetweenBullets(prefs.getInteger("bullets_cooldown"));
    }

    public void ride() {
        if (isRideLooping)
            ride(this.getDir());
    }

    public boolean isRideLooping() {
        return isRideLooping;
    }

    public void setRideLooping(boolean rideLooping) {
        this.isRideLooping = rideLooping;
    }


}
