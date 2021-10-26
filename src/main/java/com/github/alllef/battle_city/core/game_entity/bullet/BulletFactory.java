package com.github.alllef.battle_city.core.game_entity.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.util.enums.Direction;

public class BulletFactory extends EntityManager<Bullet> {

    public BulletFactory(Preferences prefs) {
        super(prefs);
    }

    public Bullet createBullet(float x, float y, Direction dir) {
        Bullet bullet = new Bullet(x, y, dir,prefs);
        entityArr.add(bullet);
        return bullet;
    }

    public Array<Bullet> updateBullets() {
        Array<Bullet> bulletsToDelete = new Array<>();
        entityArr.forEach(bullet -> {
            Sprite bulletSprite = bullet.getSprite();
            bullet.move();

            int worldSize = Gdx.app
                    .getPreferences("com.github.alllef.battle_city.prefs")
                    .getInteger("world_size");

            if (bulletSprite.getY() < 0 || bulletSprite.getX() < 0 || bulletSprite.getY() > worldSize - bulletSprite.getWidth()
                    || bulletSprite.getX() > worldSize - bulletSprite.getHeight()) {
                entityArr.removeValue(bullet, true);
                bulletsToDelete.add(bullet);
            }
        });
        return bulletsToDelete;
    }

    public void shootObstacle(Bullet bullet) {
        this.getEntities().removeValue(bullet, true);
    }

    public void shootTank(Bullet bullet) {
        this.getEntities().removeValue(bullet, true);
    }

    public void shootBullet(Bullet first, Bullet second) {
        Array<Bullet> entities = this.getEntities();
        entities.removeValue(first, true);
        entities.removeValue(second, true);
    }

    @Override
    public void update() {
        updateBullets();
        super.update();
    }
}

