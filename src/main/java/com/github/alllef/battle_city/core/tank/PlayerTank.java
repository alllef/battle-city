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

public class PlayerTank extends SingleTank implements Drawable {

    public PlayerTank() {
        super("sprites/player.png");
        addPlayerTankInputAdapter();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        this.getTankSprite().draw(spriteBatch);
    }

    public void addPlayerTankInputAdapter() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.UP -> PlayerTank.this.ride(Direction.UP);
                    case Keys.DOWN -> PlayerTank.this.ride(Direction.DOWN);
                    case Keys.RIGHT -> PlayerTank.this.ride(Direction.RIGHT);
                    case Keys.LEFT -> PlayerTank.this.ride(Direction.LEFT);
                    default -> {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                return super.keyUp(keycode);
            }
        });
    }


}
