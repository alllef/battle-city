package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.bullet.Bullet;
import com.github.alllef.battle_city.core.util.Direction;

import java.awt.*;

public abstract class SingleTank implements Tank {

    private Sprite tankSprite;
    private Direction dir = Direction.UP;
    private Direction blockedDirection = null;
    private double lastTimeShoot = TimeUtils.millis();
    private double durationBetweenBullets;

    protected SingleTank(String textureName) {
        this.tankSprite = new Sprite(new Texture(Gdx.files.internal(textureName)));
        tankSprite.setSize(2, 2);
        tankSprite.setPosition(0, 0);
    }

    @Override
    public void shoot() {
        double newTime = TimeUtils.millis();

        if (newTime - lastTimeShoot < durationBetweenBullets)
            return;

        lastTimeShoot = newTime;

        float x = tankSprite.getX();
        float y = tankSprite.getY();

        switch (dir) {
            case UP -> y += tankSprite.getHeight();
            case DOWN -> {
                y = y - tankSprite.getHeight() - 3 - 2;
                x = x - tankSprite.getHeight() - 2;
            }
            case RIGHT -> {
                x += tankSprite.getHeight();
                y = y - 2 - 2;
            }
            case LEFT -> x = x - 1 - 3 - 1 - 2;
        }

        Bullet bullet = new Bullet(x, y, dir);
    }

    @Override
    public void ride(Direction dir) {
        if (dir != blockedDirection) {
            float minDistance = 1.0f / 10f;
            this.dir = dir;

            switch (dir) {
                case UP -> tankSprite.setY(tankSprite.getY() + minDistance);
                case DOWN -> tankSprite.setY(tankSprite.getY() - minDistance);
                case RIGHT -> tankSprite.setX(tankSprite.getX() + minDistance);
                case LEFT -> tankSprite.setX(tankSprite.getX() - minDistance);
            }

            if (tankSprite.getY() < 0) tankSprite.setY(0);
            if (tankSprite.getX() < 0) tankSprite.setX(0);
            if (tankSprite.getY() > 100 - tankSprite.getWidth()) tankSprite.setY(100 - tankSprite.getWidth());
            if (tankSprite.getX() > 100 - tankSprite.getHeight()) tankSprite.setX(100 - tankSprite.getHeight());

            tankSprite.setOriginCenter();
            tankSprite.setRotation(dir.getDegree());
            blockedDirection = null;
        }
    }

    public Sprite getTankSprite() {
        return tankSprite;
    }

    public void setTankSprite(Sprite tankSprite) {
        this.tankSprite = tankSprite;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public Direction getBlockedDirection() {
        return blockedDirection;
    }

    public void setBlockedDirection(Direction blockedDirection) {
        this.blockedDirection = blockedDirection;
    }

    public void setDurationBetweenBullets(double durationBetweenBullets) {
        this.durationBetweenBullets = durationBetweenBullets;
    }

}
