package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.util.Drawable;

import java.util.List;
import java.util.function.Supplier;

public class WorldMatrix implements Drawable {
    private final GameEntity[][] entityMatrix;

    BulletFactory bulletFactory = BulletFactory.INSTANCE;
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();

    public WorldMatrix() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        entityMatrix = new GameEntity[worldSize][worldSize];
        createEntityMatrix();
    }

    private Array<GameEntity> getMovingEntitiesArray() {
        Array<GameEntity> entitiesArray = new Array<>();
        entitiesArray.addAll(bulletFactory.getBullets());
        entitiesArray.addAll(enemyTankManager.getEnemyTanks());
        entitiesArray.add(playerTank);
        return entitiesArray;
    }

    private void createEntityMatrix() {
        Array<GameEntity> entities = getMovingEntitiesArray();
        entities.addAll(obstacleGeneration.getObstacles());
        entities.forEach(this::setEntityOnMatrix);
    }

    private void updateEntityMatrix() {
        getMovingEntitiesArray().forEach(this::setEntityOnMatrix);
    }

    private void updateEntityOnMatrix(GameEntity entity, Supplier<GameEntity> supplier) {
        Sprite sprite = entity.getSprite();

        int x = (int) sprite.getX() + 1;
        int y = (int) sprite.getY() + 1;
        int boundX = x + (int) sprite.getWidth() - 1;
        int boundY = y + (int) sprite.getHeight() - 1;

        for (; x <= boundX; x++) {
            for (; y <= boundY; y++) {
                try {
                    if (supplier.get() != null && entityMatrix[x][y] != null)
                        checkOverlaps(entity, entityMatrix[x][y]);
                    else
                        entityMatrix[x][y] = supplier.get();


                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(x + " " + y);
                    System.out.println(sprite.getWidth() + " " + sprite.getHeight());
                    System.out.println(entity.toString());
                }

            }
        }
    }

    private void setEntityOnMatrix(GameEntity entity) {
        updateEntityOnMatrix(entity, () -> entity);
    }

    private void removeEntityFromMatrix(GameEntity entity) {
        updateEntityOnMatrix(entity, ()->null);
    }

    public void updateWorld() {
        bulletFactory.updateBullets().forEach(this::removeEntityFromMatrix);
        enemyTankManager.ride();
        enemyTankManager.shoot();
        playerTank.ride();
        updateEntityMatrix();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List.of(obstacleGeneration, playerTank, enemyTankManager, bulletFactory)
                .forEach(drawable -> drawable.draw(spriteBatch));
    }

    public Array<SingleTank> getAllTanks() {
        Array<SingleTank> allTanks = new Array<>();
        enemyTankManager.getEnemyTanks().forEach(allTanks::add);
        allTanks.add(playerTank);
        return allTanks;
    }

    public void checkOverlaps(GameEntity firstEntity, GameEntity secondEntity) {
        if (firstEntity instanceof Bullet bullet && secondEntity instanceof Obstacle obstacle) {
            checkBulletShootObstacle(bullet, obstacle);
        } else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof Bullet bullet) {
            checkBulletShootObstacle(bullet, obstacle);
        } else if (firstEntity instanceof Bullet bullet && secondEntity instanceof EnemyTank enemyTank) {
            checkBulletShootTank(bullet, enemyTank);
        } else if (firstEntity instanceof EnemyTank enemyTank && secondEntity instanceof Bullet bullet) {
            checkBulletShootTank(bullet, enemyTank);
        } else if (firstEntity instanceof SingleTank singleTank && secondEntity instanceof Obstacle obstacle) {
            singleTank.checkOverlapsObstacle(obstacle);
        } else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof SingleTank singleTank) {
            singleTank.checkOverlapsObstacle(obstacle);
        } else if (firstEntity instanceof SingleTank singleTank && secondEntity instanceof SingleTank singleTank1 && singleTank != singleTank1) {
            checkOverlapsTank(singleTank, singleTank1);
        } else if (firstEntity instanceof Bullet bullet && secondEntity instanceof Bullet bullet1 && bullet != bullet1) {
            checkBulletOverlapping(bullet, bullet1);
        }
        else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof Obstacle obstacle1&&obstacle!=obstacle1){
            throw new IndexOutOfBoundsException();
        }

    }

    public void checkOverlapsTank(SingleTank singleTank, SingleTank secondTank) {

        if (singleTank.getSprite().getBoundingRectangle().overlaps(secondTank.getSprite().getBoundingRectangle()))
            List.of(singleTank, secondTank).forEach(this::tankOverlapTank);

    }

    public void tankOverlapTank(SingleTank singleTank) {
        System.out.println("wtf");
        singleTank.setBlockedDirection(singleTank.getDir());
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        float minChangeDistance = prefs.getFloat("min_change_distance");
        switch (singleTank.getDir()) {
            case UP -> singleTank.getSprite().setY(singleTank.getSprite().getY() - minChangeDistance);
            case DOWN -> singleTank.getSprite().setY(singleTank.getSprite().getY() + minChangeDistance);
            case LEFT -> singleTank.getSprite().setX(singleTank.getSprite().getX() + minChangeDistance);
            case RIGHT -> singleTank.getSprite().setX(singleTank.getSprite().getX() - minChangeDistance);
        }

    }

    public void checkBulletShootTank(Bullet bullet, EnemyTank enemyTank) {
        if (bullet.getSprite().getBoundingRectangle().overlaps(enemyTank.getSprite().getBoundingRectangle())) {
            enemyTankManager.getEnemyTanks().removeValue(enemyTank, true);
            bulletFactory.getBullets().removeValue(bullet, true);
            removeEntityFromMatrix(bullet);
            removeEntityFromMatrix(enemyTank);
            //                score += prefs.getInteger("killed_tank_score");
        }
    }

    public void checkBulletOverlapping(Bullet firstBullet, Bullet secondBullet) {
        if (firstBullet.getSprite().getBoundingRectangle().overlaps(secondBullet.getSprite().getBoundingRectangle())) {
            bulletFactory.getBullets().removeValue(firstBullet, true);
            bulletFactory.getBullets().removeValue(secondBullet, true);
            removeEntityFromMatrix(firstBullet);
            removeEntityFromMatrix(secondBullet);
        }
    }

    public void checkBulletShootObstacle(Bullet bullet, Obstacle obstacle) {
        Array<Bullet> bullets = bulletFactory.getBullets();
        if (bullet.getSprite().getBoundingRectangle().overlaps(obstacle.getSprite().getBoundingRectangle())) {
            obstacleGeneration.getObstacles().removeValue(obstacle, true);
            bullets.removeValue(bullet, true);
            removeEntityFromMatrix(bullet);
            removeEntityFromMatrix(obstacle);
        }
    }
}

