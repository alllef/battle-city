package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.util.Direction;

import java.awt.*;

public abstract class SingleTank implements Tank {

    private Sprite tankSprite;

    protected SingleTank(String textureName) {
        this.tankSprite = new Sprite(new Texture(Gdx.files.internal(textureName)));
        tankSprite.setSize(2, 2);
        tankSprite.setPosition(0, 0);
    }

    @Override
    public void shoot() {
        Rectangle bullet = new Rectangle();
        bullet.setSize(2, 2);
        bullet.setLocation(0, 0);
    }

    @Override
    public void ride(Direction dir) {
        System.out.println(tankSprite.getX()+" "+tankSprite.getY());
        switch (dir) {
            case UP -> tankSprite.setY(tankSprite.getY() + 1);
            case DOWN -> tankSprite.setY(tankSprite.getY() - 1);
            case RIGHT -> tankSprite.setX(tankSprite.getX() + 1);
            case LEFT -> tankSprite.setX(tankSprite.getX() - 1);
        }
        if (tankSprite.getY()<0) tankSprite.setY(0);
        if (tankSprite.getX()<0) tankSprite.setX(0);

        tankSprite.setOrigin(tankSprite.getX(),tankSprite.getY());
        tankSprite.setRotation(dir.getDegree());

    }

    public Sprite getTankSprite() {
        return tankSprite;
    }

    public void setTankSprite(Sprite tankSprite) {
        this.tankSprite = tankSprite;
    }

}
