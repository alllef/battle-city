package com.github.alllef.battle_city.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
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
        playerTank = new PlayerTank();
        camera.setToOrtho(false, 100, 100);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float time = Gdx.graphics.getDeltaTime();
        Sprite rectangle = playerTank.getTankSprite();
        rectangle.draw(batch);
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

    }
}
