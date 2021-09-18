package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.alllef.battle_city.core.bullet.Bullet;
import com.github.alllef.battle_city.core.obstacle.Obstacle;
import com.github.alllef.battle_city.core.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.tank.*;

import java.util.List;

public class MainScreen implements Screen {
    PlayerTank playerTank;
    OrthographicCamera camera = new OrthographicCamera();
    SpriteBatch batch = new SpriteBatch();
    EnemyTankManager enemyTankManager;
    ObstacleGeneration obstacleGeneration = new ObstacleGeneration();

    public MainScreen() {
        enemyTankManager = new EnemyTankManager();
        playerTank = new PlayerTank();
        camera.setToOrtho(false, 100, 100);
        obstacleGeneration.generateObstacles(5);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        enemyTankManager.ride();
        playerTank.ride();
        Bullet.updateBullets();
        List.of(obstacleGeneration, playerTank, enemyTankManager)
                .forEach(drawable -> drawable.draw(batch));
        Bullet.bulletArray.forEach(bullet -> bullet.getBulletSprite().draw(batch));
        batch.end();
        checkBulletShootTank();
        checkBulletShootObstacle();
        checkTankOverlapsObstacle();
    }

    public Array<SingleTank> getAllTanks() {
        Array<SingleTank> allTanks = new Array<>();
        enemyTankManager.getEnemyTanks().forEach(allTanks::add);
        allTanks.add(playerTank);
        return allTanks;
    }

    public void checkBulletShootTank() {
        Array<SingleTank> allTanks = getAllTanks();
        for (Bullet bullet : Bullet.bulletArray) {
            for (SingleTank tank : allTanks) {
                if (bullet.getBulletSprite().getBoundingRectangle().overlaps(tank.getTankSprite().getBoundingRectangle())) {
                    allTanks.removeValue(tank, true);
                    Bullet.bulletArray.removeValue(bullet, true);
                }
            }
        }
    }



    public void checkTankOverlapsObstacle() {
        Array<SingleTank> allTanks = getAllTanks();
        for (SingleTank tank :allTanks) {
            for (Obstacle obstacle: obstacleGeneration.getObstacles()) {
                if (obstacle.getObstacleSprite().getBoundingRectangle().overlaps(tank.getTankSprite().getBoundingRectangle())) {
                   tank.setBlockedDirection(tank.getDir());
                    System.out.println("should be blocked");
                }
            }
        }
    }

    public void checkBulletShootObstacle() {
        for (Bullet bullet : Bullet.bulletArray) {
            for (Obstacle obstacle : obstacleGeneration.getObstacles()) {
                if (bullet.getBulletSprite().getBoundingRectangle().overlaps(obstacle.getObstacleSprite().getBoundingRectangle())) {
                    obstacleGeneration.getObstacles().removeValue(obstacle, true);
                    Bullet.bulletArray.removeValue(bullet, true);
                }
            }
        }
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
