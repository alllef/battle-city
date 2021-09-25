package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.alllef.battle_city.core.game_entities.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entities.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entities.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entities.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entities.tank.*;

import java.util.List;

public class MainScreen implements Screen {
    PlayerTank playerTank;
    OrthographicCamera camera;
    SpriteBatch batch;
    EnemyTankManager enemyTankManager;
    ObstacleGeneration obstacleGeneration;
    BitmapFont font;
    BulletFactory bulletFactory;

    int score = 0;
    Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    public MainScreen() {
        bulletFactory = BulletFactory.INSTANCE;
        camera = new OrthographicCamera();
        obstacleGeneration = new ObstacleGeneration();
        font = new BitmapFont();
        batch = new SpriteBatch();
        playerTank = new PlayerTank(bulletFactory);

        int worldSize = prefs.getInteger("world_size");

        camera.setToOrtho(false, worldSize, worldSize);

        obstacleGeneration.generateObstacles(4);
        enemyTankManager = new EnemyTankManager(5, bulletFactory);
        font.getData().setScale(prefs.getFloat("score_scale_X"), prefs.getFloat("score_scale_Y"));
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
        float scoreResultPos = prefs.getInteger("world_size") * prefs.getFloat("score_pos");
        font.draw(batch, "Score: " + score, scoreResultPos, scoreResultPos);
        List.of(obstacleGeneration, playerTank, enemyTankManager)
                .forEach(drawable -> drawable.draw(batch));

        bulletFactory.getBullets().forEach(bullet -> bullet.getSprite().draw(batch));
        batch.end();

        System.out.println(playerTank.getSprite().getX() + " " + playerTank.getSprite().getY());
        bulletFactory.updateBullets();
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
        for (Bullet bullet : bulletFactory.getBullets()) {
            for (SingleTank tank : allTanks) {
                if (bullet.getSprite().getBoundingRectangle().overlaps(tank.getSprite().getBoundingRectangle())) {
                    if (tank instanceof EnemyTank)
                        enemyTankManager.getEnemyTanks().removeValue((EnemyTank) tank, true);
                    bulletFactory.getBullets().removeValue(bullet, true);
                    score += prefs.getInteger("killed_tank_score");
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
