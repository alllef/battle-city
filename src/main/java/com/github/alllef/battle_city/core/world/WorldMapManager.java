package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.coin.CoinManager;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.ReflexEnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;
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
        rTreeMap = new RTreeMap();
        scoreManipulation = new ScoreManipulation();
        stats = new GameStats(scoreManipulation);

        bulletFactory = new BulletFactory();
        obstacleGeneration = new ObstacleGeneration(prefs.getInteger("obstacle_sets"));
        playerTankManager = new PlayerTankManager(bulletFactory);
        coinManager = new CoinManager(prefs.getInteger("coins_number"));
        enemyTankManager = new ReflexEnemyTankManager(2, 2, bulletFactory, playerTankManager, rTreeMap, scoreManipulation);

        overlapper = new Overlapper(bulletFactory, obstacleGeneration, enemyTankManager, stats);
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
        rTreeMap.createRtree(getEntities());
        overlapper.checkOverlappings(rTreeMap.getOverlappings());
        obstacleGeneration.update();
        bulletFactory.update();
        coinManager.update();
        enemyTankManager.update();
        playerTankManager.update();

    }

}
