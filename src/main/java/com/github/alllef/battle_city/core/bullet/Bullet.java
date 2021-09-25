package com.github.alllef.battle_city.core.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Bullet {
    private Sprite bulletSprite;
    private Direction dir;
    public static Array<Bullet> bulletArray = new Array<>();

    public Bullet(float x, float y, Direction dir) {
        this.dir = dir;
        SpriteParam param = SpriteParam.BULLET;
        bulletSprite = new Sprite(new Texture(param.getTexturePath()));
        bulletSprite.setSize(param.getWidth(), param.getHeight());
        bulletSprite.setOriginCenter();
        bulletSprite.setRotation(dir.getDegree());
        bulletSprite.setPosition(x, y);

        bulletArray.add(this);
    }

    public static void updateBullets() {
        bulletArray.forEach(Bullet::move);
    }

    public void move() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");

        float minDistance = prefs.getFloat("min_change_distance")* prefs.getFloat("bullet_speed_scaled");

        switch (dir) {
            case UP -> bulletSprite.setY(bulletSprite.getY() + minDistance);
            case DOWN -> bulletSprite.setY(bulletSprite.getY() - minDistance);
            case RIGHT -> bulletSprite.setX(bulletSprite.getX() + minDistance);
            case LEFT -> bulletSprite.setX(bulletSprite.getX() - minDistance);
        }

        if (bulletSprite.getY() < 0 && bulletSprite.getX() < 0 && bulletSprite.getY() > worldSize - bulletSprite.getWidth()
                && bulletSprite.getX() > worldSize - bulletSprite.getHeight())
            bulletArray.removeValue(this, true);
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
