package com.github.alllef.battle_city.core.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.alllef.battle_city.core.input_handling.MainScreenInputAdapter;
import com.github.alllef.battle_city.core.input_handling.PlayerTankInputAdapter;
import com.github.alllef.battle_city.core.path_algorithm.TankManipulation;
import com.github.alllef.battle_city.core.world.WorldMapManager;
import com.github.alllef.battle_city.core.world.stats.GameStats;

public class MainScreen implements Screen {
    OrthographicCamera camera;
    SpriteBatch batch;

    BitmapFont font;
    Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    WorldMapManager worldMapManager = WorldMapManager.getInstance();
    Game game;
    //TankManipulation tankManipulation = TankManipulation.INSTANCE;

    public MainScreen(Game game) {
        camera = new OrthographicCamera();
        font = new BitmapFont();
        batch = new SpriteBatch();
        this.game = game;
        int worldSize = prefs.getInteger("world_size");
        camera.setToOrtho(false, worldSize, worldSize);

        InputMultiplexer multiplexer = new InputMultiplexer();
        // multiplexer.addProcessor(new MainScreenInputAdapter());
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
        // tankManipulation.update();

        batch.begin();
        font.getData().setScale(0.25f, 0.25f);
        // font.draw(batch,"Algo type: " +tankManipulation.getAlgoType().name(),50,50);
        worldMapManager.draw(batch);
        //tankManipulation.draw(batch);
        batch.end();

        if (stats.isGameOver())
            game.setScreen(new MainScreen(game));
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
        font.dispose();
    }
}
