package com.github.alllef.battle_city.core.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.Direction;

public class Bullet {
    private Sprite bulletSprite;
    private Direction dir;
    public static Array<Bullet> bulletArray = new Array<>();

    public static void updateBullets() {
        bulletArray.forEach(Bullet::move);
    }

    public void move() {
        float minDistance = 1.0f / 5f;

        switch (dir) {
            case UP -> bulletSprite.setY(bulletSprite.getY() + minDistance);
            case DOWN -> bulletSprite.setY(bulletSprite.getY() - minDistance);
            case RIGHT -> bulletSprite.setX(bulletSprite.getX() + minDistance);
            case LEFT -> bulletSprite.setX(bulletSprite.getX() - minDistance);
        }

        if (bulletSprite.getY() < 0 && bulletSprite.getX() < 0 && bulletSprite.getY() > 100 - bulletSprite.getWidth()
                && bulletSprite.getX() > 100 - bulletSprite.getHeight())
            bulletArray.removeValue(this, true);
    }

    public Bullet(float x, float y, Direction dir) {
        this.dir = dir;

        bulletSprite = new Sprite(new Texture(Gdx.files.internal("sprites/bullet.png")));
        bulletSprite.setSize(2, 2);
        bulletSprite.setPosition(x, y);
        bulletSprite.setRotation(dir.getDegree());

        bulletArray.add(this);
    }

    public Sprite getBulletSprite() {
        return bulletSprite;
    }

    public void setBulletSprite(Sprite bulletSprite) {
        this.bulletSprite = bulletSprite;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }
}
