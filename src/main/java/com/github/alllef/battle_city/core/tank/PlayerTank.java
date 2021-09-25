package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;

import static com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.SpriteParam;

import java.util.Optional;

public class PlayerTank extends SingleTank implements Drawable {
    private boolean isRideLooping = false;

    public PlayerTank() {
        super(SpriteParam.PLAYER_TANK.getTexturePath());
        addPlayerTankInputAdapter();
        setDurationBetweenBullets(prefs.getInteger("bullets_cooldown"));
    }


    @Override
    public void draw(SpriteBatch spriteBatch) {
        this.getTankSprite().draw(spriteBatch);
    }

    public void ride() {
        if (isRideLooping)
            ride(this.getDir());
    }

    public void addPlayerTankInputAdapter() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.SPACE)
                    PlayerTank.this.shoot();

                Optional<Direction> optionalDir = Direction.of(keycode);

                if (optionalDir.isPresent()) {
                    PlayerTank.this.setRideLooping(true);
                    PlayerTank.this.ride(optionalDir.get());
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                Optional<Direction> dir = Direction.of(keycode);
                if (dir.isPresent() && dir.get() == PlayerTank.this.getDir())
                    PlayerTank.this.setRideLooping(false);
                return false;
            }
        });
    }

    public boolean isRideLooping() {
        return isRideLooping;
    }

    public void setRideLooping(boolean rideLooping) {
        this.isRideLooping = rideLooping;
    }
}
