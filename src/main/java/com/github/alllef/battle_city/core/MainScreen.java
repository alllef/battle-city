package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.alllef.battle_city.core.bullet.Bullet;
import com.github.alllef.battle_city.core.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.tank.EnemyTank;
import com.github.alllef.battle_city.core.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.tank.PlayerTank;
import com.github.alllef.battle_city.core.util.Drawable;

import java.awt.*;
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
        obstacleGeneration.generateObstacles(15);
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
