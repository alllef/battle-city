package com.github.alllef.battle_city.core.game_entity.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.SpriteParam;

public abstract class SingleTank extends GameEntity implements Tank {

    private Direction dir = Direction.UP;
    private Direction blockedDirection = null;
    private double lastTimeShoot = TimeUtils.millis();
    private double durationBetweenBullets;
    private final BulletFactory bulletFactory;

    protected final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    protected SingleTank(String textureName, BulletFactory bulletFactory) {
        this.bulletFactory = bulletFactory;
        this.sprite = new Sprite(new Texture(Gdx.files.internal(textureName)));
        sprite.setSize(SpriteParam.SINGLE_TANK.getWidth(), SpriteParam.SINGLE_TANK.getHeight());
        sprite.setPosition(0, 0);
        sprite.setOriginCenter();
    }

    @Override
    public void shoot() {

        if (!prefs.getBoolean("enable_shooting"))
            return;

        double newTime = TimeUtils.millis();
        if (newTime - lastTimeShoot < durationBetweenBullets)
            return;
        lastTimeShoot = newTime;

        float x = sprite.getX();
        float y = sprite.getY();

        switch (dir) {
            case UP -> y += sprite.getHeight();
            case DOWN -> y -= sprite.getHeight();
            case RIGHT -> x += sprite.getWidth();
            case LEFT -> x -= sprite.getWidth();
        }
        bulletFactory.createBullet(x, y, dir);
    }

    @Override
    public void ride(Direction dir) {
        if (dir == blockedDirection) {
            blockedDirection = null;
            return;
        }

        setDir(dir);
        float minDistance = prefs.getFloat("min_change_distance");

        switch (dir) {
            case UP -> sprite.setY(sprite.getY() + minDistance);
            case DOWN -> sprite.setY(sprite.getY() - minDistance);
            case RIGHT -> sprite.setX(sprite.getX() + minDistance);
            case LEFT -> sprite.setX(sprite.getX() - minDistance);
        }

        int worldSize = prefs.getInteger("world_size");

        if (sprite.getY() < 0) sprite.setY(0);
        if (sprite.getX() < 0) sprite.setX(0);
        if (sprite.getY() > worldSize - sprite.getWidth())
            sprite.setY(worldSize - sprite.getWidth());
        if (sprite.getX() > worldSize - sprite.getHeight())
            sprite.setX(worldSize - sprite.getHeight());

    }

    public void checkOverlapsObstacle(Obstacle obstacle) {

        if (obstacle.getSprite().getBoundingRectangle().overlaps(this.getSprite().getBoundingRectangle())) {
            this.setBlockedDirection(this.getDir());
            float minChangeDistance = prefs.getFloat("min_change_distance");
            switch (this.getDir()) {
                case UP -> this.getSprite().setY(this.getSprite().getY() - minChangeDistance);
                case DOWN -> this.getSprite().setY(this.getSprite().getY() + minChangeDistance);
                case LEFT -> this.getSprite().setX(this.getSprite().getX() + minChangeDistance);
                case RIGHT -> this.getSprite().setX(this.getSprite().getX() - minChangeDistance);
            }

        }
    }


    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        if (dir != this.dir) {
            this.dir = dir;
            sprite.setRotation(dir.getDegree());
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SingleTank)) return false;
        if (!super.equals(o)) return false;

        SingleTank that = (SingleTank) o;

        return getDir() == that.getDir();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getDir() != null ? getDir().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SingleTank{" +
                "dir=" + dir +
                ", blockedDirection=" + blockedDirection +
                ", lastTimeShoot=" + lastTimeShoot +
                ", durationBetweenBullets=" + durationBetweenBullets +
                ", bulletFactory=" + bulletFactory +
                ", prefs=" + prefs +
                '}';
    }
}
