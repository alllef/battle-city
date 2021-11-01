package com.github.alllef.battle_city.core.game_entity.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.MovingEntity;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.enums.Move;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;

import java.util.Objects;

public abstract class SingleTank extends MovingEntity implements Tank {

    private Direction blockedDirection = null;
    private double lastTimeShoot = TimeUtils.millis();
    private double durationBetweenBullets;
    private final BulletFactory bulletFactory;

    protected SingleTank(float posX, float posY, SpriteParam param, BulletFactory bulletFactory, Preferences prefs) {
        super(posX, posY, param, Direction.UP, prefs);
        this.bulletFactory = bulletFactory;
    }

    @Override
    protected Sprite spriteConfigure(float posX, float posY, SpriteParam param) {
        Sprite tmp = super.spriteConfigure(posX, posY, param);
        tmp.setOriginCenter();
        return tmp;
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
            this.setDir(dir);
            sprite.setRotation(dir.getDegree());
            return;
        }

        float minDist = prefs.getFloat("min_change_distance");
        this.move(Move.FORWARD, minDist);

        int worldSize = prefs.getInteger("world_size");

        if (sprite.getY() < 0) sprite.setY(0);
        if (sprite.getX() < 0) sprite.setX(0);
        if (sprite.getY() > worldSize - sprite.getWidth())
            sprite.setY(worldSize - sprite.getWidth());
        if (sprite.getX() > worldSize - sprite.getHeight())
            sprite.setX(worldSize - sprite.getHeight());

    }

    public void overlapsObstacle() {
        this.setBlockedDirection(this.getDir());
        returnMinDistance();
    }

    public void overlapsTank() {
        this.setBlockedDirection(this.getDir());
        returnMinDistance();
    }

    private void returnMinDistance() {
        float minDist = prefs.getFloat("min_change_distance");
        this.move(Move.BACKWARD, minDist);
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
