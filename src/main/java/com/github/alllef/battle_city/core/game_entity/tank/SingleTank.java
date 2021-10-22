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

import java.util.Objects;

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
            case UP -> y += sprite.getHeight() + 0.2f;
            case DOWN -> y -= (sprite.getHeight() + 0.2f);
            case RIGHT -> x += (sprite.getWidth() + 0.2f);
            case LEFT -> x -= (sprite.getWidth() + 0.2f);
        }

        bulletFactory.createBullet(x, y, dir);
    }

    @Override
    public void ride(Direction dir) {
        if (dir == blockedDirection) {
            blockedDirection = null;
            return;
        }

        if (this.dir != dir) {
            setDir(dir);
            return;
        }

        float minDist = prefs.getFloat("min_change_distance");

        switch (this.getDir()) {
            case UP -> sprite.setY(sprite.getY() + minDist);
            case DOWN -> sprite.setY(sprite.getY() - minDist);
            case RIGHT -> sprite.setX(sprite.getX() + minDist);
            case LEFT -> sprite.setX(sprite.getX() - minDist);
        }

        int worldSize = prefs.getInteger("world_size");

        if (sprite.getY() < 0) sprite.setY(0);
        if (sprite.getX() < 0) sprite.setX(0);
        if (sprite.getY() > worldSize - sprite.getWidth())
            sprite.setY(worldSize - sprite.getWidth());
        if (sprite.getX() > worldSize - sprite.getHeight())
            sprite.setX(worldSize - sprite.getHeight());

    }

    public void overlapsObstacle(Obstacle obstacle) {

        this.setBlockedDirection(this.getDir());
        returnMinDistance();
    }

    public void overlapsTank() {
        this.setBlockedDirection(this.getDir());
        returnMinDistance();
    }

    private void returnMinDistance() {
        float minDist = prefs.getFloat("min_change_distance");

        switch (this.getDir()) {
            case UP -> sprite.setY(sprite.getY() - minDist);
            case DOWN -> sprite.setY(sprite.getY() + minDist);
            case RIGHT -> sprite.setX(sprite.getX() - minDist);
            case LEFT -> sprite.setX(sprite.getX() + minDist);
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

        if (Double.compare(that.lastTimeShoot, lastTimeShoot) != 0) return false;
        if (Double.compare(that.durationBetweenBullets, durationBetweenBullets) != 0) return false;
        if (getDir() != that.getDir()) return false;
        if (getBlockedDirection() != that.getBlockedDirection()) return false;
        if (!Objects.equals(bulletFactory, that.bulletFactory))
            return false;
        return Objects.equals(prefs, that.prefs);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (getDir() != null ? getDir().hashCode() : 0);
        result = 31 * result + (getBlockedDirection() != null ? getBlockedDirection().hashCode() : 0);
        temp = Double.doubleToLongBits(lastTimeShoot);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(durationBetweenBullets);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (bulletFactory != null ? bulletFactory.hashCode() : 0);
        result = 31 * result + (prefs != null ? prefs.hashCode() : 0);
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
