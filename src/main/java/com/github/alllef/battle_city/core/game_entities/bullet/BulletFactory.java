package com.github.alllef.battle_city.core.game_entities.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;

public enum BulletFactory implements Drawable {
    INSTANCE;
    public Array<Bullet> bulletArray = new Array<>();

    public Bullet createBullet(float x, float y, Direction dir) {
        Bullet bullet = new Bullet(x, y, dir);
        bulletArray.add(bullet);
        return bullet;
    }

    public Array<Bullet> getBullets() {
        return bulletArray;
    }

    public void updateBullets() {
        bulletArray.forEach(bullet -> {
            Sprite bulletSprite = bullet.getSprite();
            bullet.move();

            int worldSize = Gdx.app
                    .getPreferences("com.github.alllef.battle_city.prefs")
                    .getInteger("world_size");

            if (bulletSprite.getY() < 0 && bulletSprite.getX() < 0 && bulletSprite.getY() > worldSize - bulletSprite.getWidth()
                    && bulletSprite.getX() > worldSize - bulletSprite.getHeight())
                bulletArray.removeValue(bullet, true);
        });
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        getBullets().forEach(bullet -> bullet.getSprite().draw(spriteBatch));
    }
}
