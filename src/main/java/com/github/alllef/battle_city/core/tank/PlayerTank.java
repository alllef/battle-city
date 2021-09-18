package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;

import static com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;

import java.awt.*;
import java.util.Optional;

public class PlayerTank extends SingleTank implements Drawable {
    private boolean rideLooping = false;

    public PlayerTank() {
        super("sprites/player.png");
        addPlayerTankInputAdapter();
        setDurationBetweenBullets(500);
    }


    @Override
    public void draw(SpriteBatch spriteBatch) {
        this.getTankSprite().draw(spriteBatch);
    }

    public void ride() {
        if (rideLooping)
            ride(this.getDir());
    }

    public void addPlayerTankInputAdapter() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.SPACE){
                    PlayerTank.this.shoot();
                    System.out.println("Generated");
                }

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
                if (dir.isPresent() && dir.get() == PlayerTank.this.getDir()) {
                    PlayerTank.this.setRideLooping(false);
                }
                return false;
            }
        });
    }

    public boolean isRideLooping() {
        return rideLooping;
    }

    public void setRideLooping(boolean rideLooping) {
        this.rideLooping = rideLooping;
    }
}
