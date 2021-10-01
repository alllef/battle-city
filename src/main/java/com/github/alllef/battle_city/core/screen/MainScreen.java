package com.github.alllef.battle_city.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.alllef.battle_city.core.input_handling.MainScreenInputAdapter;
import com.github.alllef.battle_city.core.input_handling.PlayerTankInputAdapter;
import com.github.alllef.battle_city.core.path_algorithm.TankManipulation;
import com.github.alllef.battle_city.core.world.RTreeMap;
import com.github.alllef.battle_city.core.world.WorldMapManager;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class MainScreen implements Screen {
    OrthographicCamera camera;
    SpriteBatch batch;

    BitmapFont font;
    Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    WorldMapManager worldMapManager = WorldMapManager.getInstance();
    TankManipulation tankManipulation = TankManipulation.INSTANCE;
    int score = 0;

    public MainScreen() {
        camera = new OrthographicCamera();
        font = new BitmapFont();
        batch = new SpriteBatch();

        int worldSize = prefs.getInteger("world_size");
        camera.setToOrtho(false, worldSize, worldSize);
        font.getData().setScale(prefs.getFloat("score_scale_X"), prefs.getFloat("score_scale_Y"));

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new MainScreenInputAdapter());
        multiplexer.addProcessor(new PlayerTankInputAdapter());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float v) {

        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        worldMapManager.update();
        tankManipulation.update();

        batch.begin();
        float scoreResultPos = prefs.getInteger("world_size") * prefs.getFloat("score_pos");
        font.draw(batch, "Score: " + score, scoreResultPos, scoreResultPos);
        worldMapManager.draw(batch);
        tankManipulation.draw(batch);
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
