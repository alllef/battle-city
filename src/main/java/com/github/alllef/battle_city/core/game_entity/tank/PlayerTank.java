package com.github.alllef.battle_city.core.game_entity.tank;

import com.badlogic.gdx.Gdx;

import static com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.path_algorithm.TankManipulation;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.AlgoType;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.SpriteParam;

import java.util.Optional;

public class PlayerTank extends SingleTank implements Drawable {
    private static final PlayerTank tank = new PlayerTank(BulletFactory.INSTANCE);

    public static PlayerTank getInstance() {
        return tank;
    }

    private boolean isRideLooping = false;

    private PlayerTank(BulletFactory bulletFactory) {
        super(SpriteParam.PLAYER_TANK.getTexturePath(), bulletFactory);
        setDurationBetweenBullets(prefs.getInteger("bullets_cooldown"));
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        this.getSprite().draw(spriteBatch);
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
