package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.bullet.Bullet;
import com.github.alllef.battle_city.core.bullet.BulletFactory;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.SpriteParam;

import java.awt.*;

public abstract class SingleTank implements Tank {

    private Sprite tankSprite;
    private Direction dir = Direction.UP;
    private Direction blockedDirection = null;
    private double lastTimeShoot = TimeUtils.millis();
    private double durationBetweenBullets;
    private final BulletFactory bulletFactory;

    protected final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    protected SingleTank(String textureName, BulletFactory bulletFactory) {
        this.bulletFactory = bulletFactory;
        this.tankSprite = new Sprite(new Texture(Gdx.files.internal(textureName)));
        tankSprite.setSize(SpriteParam.SINGLE_TANK.getWidth(), SpriteParam.SINGLE_TANK.getHeight());
        tankSprite.setPosition(0, 0);
        tankSprite.setOriginCenter();
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
            case DOWN -> y -= tankSprite.getHeight();
            case RIGHT -> x += tankSprite.getWidth();
            case LEFT -> x -= tankSprite.getWidth();
        }
        bulletFactory.createBullet(x, y, dir);
    }

    @Override
    public void ride(Direction dir) {
        if (dir == blockedDirection)
            return;

        if (dir != this.dir) {
            this.dir = dir;
            tankSprite.setRotation(dir.getDegree());
            return;
        }

        float minDistance = prefs.getFloat("min_change_distance");

        switch (dir) {
            case UP -> tankSprite.setY(tankSprite.getY() + minDistance);
            case DOWN -> tankSprite.setY(tankSprite.getY() - minDistance);
            case RIGHT -> tankSprite.setX(tankSprite.getX() + minDistance);
            case LEFT -> tankSprite.setX(tankSprite.getX() - minDistance);
        }

        int worldSize = prefs.getInteger("world_size");
        if (tankSprite.getY() < 0) tankSprite.setY(0);
        if (tankSprite.getX() < 0) tankSprite.setX(0);
        if (tankSprite.getY() > worldSize - tankSprite.getWidth())
            tankSprite.setY(worldSize - tankSprite.getWidth());
        if (tankSprite.getX() > worldSize - tankSprite.getHeight())
            tankSprite.setX(worldSize - tankSprite.getHeight());

        blockedDirection = null;
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
