package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.alllef.battle_city.core.tank.EnemyTank;
import com.github.alllef.battle_city.core.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.tank.PlayerTank;

import java.awt.*;

public class MainScreen implements Screen {
    PlayerTank playerTank;
    OrthographicCamera camera = new OrthographicCamera();
    SpriteBatch batch = new SpriteBatch();
    EnemyTankManager enemyTankManager;

    public MainScreen() {
        enemyTankManager = new EnemyTankManager();
        playerTank = new PlayerTank();
        camera.setToOrtho(false, 100, 100);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        enemyTankManager.ride();
        batch.begin();
        enemyTankManager.getEnemyTanks().forEach(enemyTank -> {
        Sprite sprite =enemyTank.getTankSprite();
          batch.draw(sprite.getTexture(),sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
        });
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
