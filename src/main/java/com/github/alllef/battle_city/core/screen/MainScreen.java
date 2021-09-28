package com.github.alllef.battle_city.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.alllef.battle_city.core.world.RtreeMap;

public class MainScreen implements Screen {
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;
    Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    RtreeMap rtreeMatrix = new RtreeMap();
    int score = 0;

    public MainScreen() {
        camera = new OrthographicCamera();
        font = new BitmapFont();
        batch = new SpriteBatch();

        int worldSize = prefs.getInteger("world_size");
        camera.setToOrtho(false, worldSize, worldSize);
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
        rtreeMatrix.draw(batch);
        batch.end();
        rtreeMatrix.updateRtree();

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
