package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class PlayerTank implements Tank {
   private Texture bullet;
   private Rectangle rectangle;

    public PlayerTank() {
        this.bullet = new Texture(Gdx.files.internal("sprites/bullet.png"));
        this.rectangle = new Rectangle(50,50,10,10);
    }

    public Texture getBullet() {
        return bullet;
    }

    public void setBullet(Texture bullet) {
        this.bullet = bullet;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void shoot() {
        Rectangle bullet = new Rectangle();
        bullet.setSize(2, 2);
        bullet.setLocation(1, 1);

    }

    @Override
    public void ride(Direction dir) {

    }

}
