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

public class WorldMatrix implements Drawable {
    private final GameEntity[][] entityMatrix;

    BulletFactory bulletFactory = BulletFactory.INSTANCE;
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();
    Array<GameEntity> entitiesArray;

    public WorldMatrix() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        entityMatrix = new GameEntity[worldSize][worldSize];

    }

    private Array<GameEntity> getEntitiesArray() {
        Array<GameEntity> entitiesArray = new Array<>();
        entitiesArray.addAll(bulletFactory.getBullets());
        entitiesArray.addAll(enemyTankManager.getEnemyTanks());
        entitiesArray.addAll(obstacleGeneration.getObstacles());
        entitiesArray.add(playerTank);
        return entitiesArray;
    }

    private void updateEntityMatrix() {
        entitiesArray = getEntitiesArray();
        entitiesArray.forEach(this::setEntityOnMatrix);
    }

    private void updateEntityOnMatrix(GameEntity entityCoords, GameEntity setEntity) {
        Sprite sprite = entityCoords.getSprite();

        int x = (int) sprite.getX();
        int y = (int) sprite.getY();
        int boundX = x + (int) sprite.getWidth();
        int boundY = y + (int) sprite.getHeight();

        for (; x < boundX; x++) {
            for (; y < boundY; y++) {
                try {
                    if (setEntity!=null&&entityMatrix[x][y] != null)
                        checkOverlaps(entityCoords, entityMatrix[x][y]);
                    else
                        entityMatrix[x][y] = setEntity;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(x+" "+y);
                    System.out.println(sprite.getWidth()+" "+sprite.getHeight());
                }

            }
        }
    }

    private void setEntityOnMatrix(GameEntity entity) {
        updateEntityOnMatrix(entity, entity);
    }

    private void removeEntityFromMatrix(GameEntity entity) {
        updateEntityOnMatrix(entity, null);
    }

    public void updateWorld() {
        updateEntityMatrix();
        bulletFactory.updateBullets();
        enemyTankManager.ride();
        enemyTankManager.shoot();
        playerTank.ride();
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
        if (firstEntity instanceof Bullet bullet && secondEntity instanceof Obstacle obstacle)
            checkBulletShootObstacle(bullet, obstacle);

        else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof Bullet bullet)
            checkBulletShootObstacle(bullet, obstacle);

        else if (firstEntity instanceof Bullet bullet && secondEntity instanceof EnemyTank enemyTank)
            checkBulletShootTank(bullet, enemyTank);

        else if (firstEntity instanceof EnemyTank enemyTank && secondEntity instanceof Bullet bullet)
            checkBulletShootTank(bullet, enemyTank);

        else if (firstEntity instanceof SingleTank singleTank && secondEntity instanceof Obstacle obstacle)
            singleTank.checkOverlapsObstacle(obstacle);

        else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof SingleTank singleTank)
            singleTank.checkOverlapsObstacle(obstacle);
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

