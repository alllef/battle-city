package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.util.Direction;

import java.awt.*;

public class PlayerTank implements Tank {

    Sprite tankSprite = new Sprite(new Texture(Gdx.files.internal("sprites/enemy.png")));

    public PlayerTank() {
        tankSprite.setSize(10, 10);
        tankSprite.setPosition(0, 0);

    }

    @Override
    public void shoot() {
        Rectangle bullet = new Rectangle();
        bullet.setSize(2, 2);
        bullet.setLocation(1, 1);
    }

    @Override
    public void ride(Direction dir) {
        switch (dir) {
            case UP -> tankSprite.setY(tankSprite.getY()+1);
            case DOWN -> tankSprite.setY(tankSprite.getY()-1);
            case RIGHT -> tankSprite.setX(tankSprite.getX()+1);
            case LEFT -> tankSprite.setX(tankSprite.getX()-1);
        }
    }

    public Sprite getTankSprite() {
        return tankSprite;
    }`

    public void setTankSprite(Sprite tankSprite) {
        this.tankSprite = tankSprite;
    }
}
