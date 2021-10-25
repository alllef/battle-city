package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.coin.CoinManager;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.ReflexEnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;
import com.github.alllef.battle_city.core.util.interfaces.Drawable;
import com.github.alllef.battle_city.core.util.interfaces.Updatable;
import com.github.alllef.battle_city.core.world.score.ScoreManipulation;

import java.util.ArrayList;
import java.util.List;

public class WorldMapManager implements Drawable, Updatable {

    private static final WorldMapManager worldMapManager = new WorldMapManager();

    public static WorldMapManager getInstance() {
        return worldMapManager;
    }

    private final RTreeMap rTreeMap = RTreeMap.getInstance();

    private final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    private final BulletFactory bulletFactory = BulletFactory.getInstance();
    private final ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    private final PlayerTankManager playerTankManager = PlayerTankManager.getInstance();
    private final CoinManager coinManager = CoinManager.getInstance();
    private final ScoreManipulation scoreManipulation = ScoreManipulation.INSTANCE;
    private final ReflexEnemyTankManager enemyTankManager = ReflexEnemyTankManager.getInstance();

    protected List<GameEntity> getEntities() {
        List<GameEntity> entitiesArray = new ArrayList<>();
        List<EntityManager<? extends GameEntity>> managers = List.of(bulletFactory, enemyTankManager, playerTankManager, obstacleGeneration);

        for (EntityManager<? extends GameEntity> manager : managers) {
            for (GameEntity entity : manager.getEntities())
                entitiesArray.add(entity);
        }

        return entitiesArray;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List.of(obstacleGeneration, bulletFactory, coinManager, enemyTankManager, playerTankManager, scoreManipulation)
                .forEach(drawable -> drawable.draw(spriteBatch));
    }

    @Override
    public void update() {
        List.of(obstacleGeneration, bulletFactory, coinManager, enemyTankManager, playerTankManager)
                .forEach(Updatable::update);

        rTreeMap.createRtree(getEntities());
        rTreeMap.checkOverlappings();
    }

}
