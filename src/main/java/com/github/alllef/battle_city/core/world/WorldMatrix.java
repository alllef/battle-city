package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entities.GameEntity;
import com.github.alllef.battle_city.core.game_entities.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entities.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entities.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entities.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entities.tank.EnemyTank;
import com.github.alllef.battle_city.core.game_entities.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entities.tank.PlayerTank;
import com.github.alllef.battle_city.core.game_entities.tank.SingleTank;
import com.github.alllef.battle_city.core.util.Drawable;

import java.util.List;

public class WorldMatrix implements Drawable {
    GameEntity[][] entityMatrix;

    BulletFactory bulletFactory = BulletFactory.INSTANCE;
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();

    public WorldMatrix() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        entityMatrix = new GameEntity[worldSize][worldSize];
    }

    private void setEntityOnMatrix(GameEntity entity) {
        Sprite sprite = entity.getSprite();
        for (int x = (int) sprite.getX(); x < x + (int) sprite.getWidth(); x++) {
            for (int y = (int) sprite.getY(); y < y + (int) sprite.getWidth(); y++) {
                entityMatrix[x][y] = entity;
            }
        }
    }

    public void updateWorld(){
        bulletFactory.updateBullets();
        enemyTankManager.ride();
        enemyTankManager.shoot();
        playerTank.ride();

        checkBulletShootTank();
        checkBulletShootObstacle();
        checkTankOverlapsObstacle();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List.of(obstacleGeneration, playerTank, enemyTankManager,bulletFactory)
                .forEach(drawable -> drawable.draw(spriteBatch));
    }

    public Array<SingleTank> getAllTanks() {
        Array<SingleTank> allTanks = new Array<>();
        enemyTankManager.getEnemyTanks().forEach(allTanks::add);
        allTanks.add(playerTank);
        return allTanks;
    }

    public void checkBulletShootTank() {
        Array<SingleTank> allTanks = getAllTanks();
        for (Bullet bullet : bulletFactory.getBullets()) {
            for (SingleTank tank : allTanks) {
                if (bullet.getSprite().getBoundingRectangle().overlaps(tank.getSprite().getBoundingRectangle())) {
                    if (tank instanceof EnemyTank)
                        enemyTankManager.getEnemyTanks().removeValue((EnemyTank) tank, true);
                    bulletFactory.getBullets().removeValue(bullet, true);
    //                score += prefs.getInteger("killed_tank_score");
                }
            }
        }
    }


    public void checkTankOverlapsObstacle() {
        Array<SingleTank> allTanks = getAllTanks();
        for (SingleTank tank : allTanks) {
            for (Obstacle obstacle : obstacleGeneration.getObstacles()) {
                if (obstacle.getSprite().getBoundingRectangle().overlaps(tank.getSprite().getBoundingRectangle())) {
                    tank.setBlockedDirection(tank.getDir());
                    switch (tank.getDir()) {
                        case UP -> tank.getSprite().setY(tank.getSprite().getY() - 0.1f);
                        case DOWN -> tank.getSprite().setY(tank.getSprite().getY() + 0.1f);
                        case LEFT -> tank.getSprite().setX(tank.getSprite().getX() + 0.1f);
                        case RIGHT -> tank.getSprite().setX(tank.getSprite().getX() - 0.1f);
                    }
                    System.out.println(tank.getSprite().getX() + " " + tank.getSprite().getY());
                }
            }
        }
    }

    public void checkBulletShootObstacle() {
        Array<Bullet> bullets = bulletFactory.getBullets();
        for (Bullet bullet : bullets) {
            for (Obstacle obstacle : obstacleGeneration.getObstacles()) {
                if (bullet.getSprite().getBoundingRectangle().overlaps(obstacle.getSprite().getBoundingRectangle())) {
                    obstacleGeneration.getObstacles().removeValue(obstacle, true);
                    bullets.removeValue(bullet, true);
                }
            }
        }
    }
}
