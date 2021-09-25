package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    OrthographicCamera camera;
    SpriteBatch batch;
    EnemyTankManager enemyTankManager;
    ObstacleGeneration obstacleGeneration;
    BitmapFont font;
    int score = 0;
    Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    public MainScreen() {
        camera = new OrthographicCamera();
        obstacleGeneration = new ObstacleGeneration();
        font = new BitmapFont();
        batch = new SpriteBatch();
        playerTank = new PlayerTank();


        int worldSize = prefs.getInteger("world_size");

        camera.setToOrtho(false, worldSize, worldSize);

        obstacleGeneration.generateObstacles(4);
        enemyTankManager = new EnemyTankManager(5);
        font.getData().setScale(prefs.getFloat("score_scale_X"),prefs.getFloat("score_scale_Y"));
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
        float scoreResultPos = prefs.getInteger("world_size")*prefs.getFloat("score_pos");
        font.draw(batch, "Score: " + score, scoreResultPos, scoreResultPos);
        List.of(obstacleGeneration, playerTank, enemyTankManager)
                .forEach(drawable -> drawable.draw(batch));

        Bullet.bulletArray.forEach(bullet -> bullet.getBulletSprite().draw(batch));
        batch.end();

        System.out.println(playerTank.getTankSprite().getX() + " " + playerTank.getTankSprite().getY());
        Bullet.updateBullets();
        enemyTankManager.ride();
        enemyTankManager.shoot();
        playerTank.ride();

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
                    if (tank instanceof EnemyTank)
                        enemyTankManager.getEnemyTanks().removeValue((EnemyTank) tank, true);
                    Bullet.bulletArray.removeValue(bullet, true);
                    score += prefs.getInteger("killed_tank_score");
                }
            }
        }
    }


    public void checkTankOverlapsObstacle() {
        Array<SingleTank> allTanks = getAllTanks();
        for (SingleTank tank : allTanks) {
            for (Obstacle obstacle : obstacleGeneration.getObstacles()) {
                if (obstacle.getObstacleSprite().getBoundingRectangle().overlaps(tank.getTankSprite().getBoundingRectangle())) {
                    tank.setBlockedDirection(tank.getDir());
                    System.out.println(tank.getTankSprite().getX() + " " + tank.getTankSprite().getY());
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
