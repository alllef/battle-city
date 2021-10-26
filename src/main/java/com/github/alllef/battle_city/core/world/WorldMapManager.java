package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.coin.CoinManager;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.ReflexEnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;
import com.github.alllef.battle_city.core.input_handling.PlayerTankInputAdapter;
import com.github.alllef.battle_city.core.util.interfaces.Drawable;
import com.github.alllef.battle_city.core.util.interfaces.Updatable;
import com.github.alllef.battle_city.core.world.overlap.Overlapper;
import com.github.alllef.battle_city.core.world.stats.GameStats;
import com.github.alllef.battle_city.core.world.stats.ScoreManipulation;

import java.util.ArrayList;
import java.util.List;

public class WorldMapManager implements Drawable, Updatable {

    private static final WorldMapManager worldMapManager = new WorldMapManager();

    public static WorldMapManager getInstance() {
        return worldMapManager;
    }

    private RTreeMap rTreeMap;
    private Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    private BulletFactory bulletFactory;
    private ObstacleGeneration obstacleGeneration;
    private PlayerTankManager playerTankManager;
    private CoinManager coinManager;
    private ScoreManipulation scoreManipulation;
    private ReflexEnemyTankManager enemyTankManager;
    private Overlapper overlapper;
    private GameStats stats;

    protected List<GameEntity> getEntities() {
        List<GameEntity> entitiesArray = new ArrayList<>();
        var managers = List.of(bulletFactory, enemyTankManager, playerTankManager, obstacleGeneration);

        for (var manager : managers) {
            for (GameEntity entity : manager.getEntities())
                entitiesArray.add(entity);
        }

        return entitiesArray;
    }


    public void initialize() {
        rTreeMap = new RTreeMap(prefs);
        scoreManipulation = new ScoreManipulation(prefs);
        stats = new GameStats(scoreManipulation, prefs);

        bulletFactory = new BulletFactory(prefs);
        obstacleGeneration = new ObstacleGeneration(prefs.getInteger("obstacle_sets"), prefs);
        playerTankManager = new PlayerTankManager(bulletFactory, prefs);
        coinManager = new CoinManager(prefs.getInteger("coins_number"), prefs);
        enemyTankManager = new ReflexEnemyTankManager(prefs.getInteger("random_reflex_tank_number"), prefs.getInteger("player_reflex_tank_number"), bulletFactory, playerTankManager, rTreeMap, scoreManipulation, prefs);

        overlapper = new Overlapper(bulletFactory, obstacleGeneration, enemyTankManager, stats);
        configureInputHandling(playerTankManager);
    }

    public void configureInputHandling(PlayerTankManager playerTankManager) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // multiplexer.addProcessor(new MainScreenInputAdapter());
        multiplexer.addProcessor(new PlayerTankInputAdapter(playerTankManager));
        Gdx.input.setInputProcessor(multiplexer);
    }

    public boolean isGameOver() {
        return stats.isGameOver();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List.of(obstacleGeneration, bulletFactory, coinManager, enemyTankManager, playerTankManager, scoreManipulation)
                .forEach(drawable -> drawable.draw(spriteBatch));
    }

    @Override
    public void update() {

        obstacleGeneration.update();
        bulletFactory.update();
        coinManager.update();
        enemyTankManager.update();
        playerTankManager.update();

        rTreeMap.createRtree(getEntities());
        overlapper.checkOverlappings(rTreeMap.getOverlappings());

    }

}
