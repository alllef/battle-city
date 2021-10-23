package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.coin.CoinManager;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.Updatable;
import com.github.alllef.battle_city.core.world.score.ScoreManipulation;

import java.util.List;

public class WorldMapManager implements Drawable, Updatable {

    private static final WorldMapManager worldMapManager = new WorldMapManager();

    public static WorldMapManager getInstance() {
        return worldMapManager;
    }

    private final RTreeMap rTreeMap = RTreeMap.getInstance();

    protected final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    protected final BulletFactory bulletFactory = BulletFactory.getInstance();
    protected final ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    protected final PlayerTankManager playerTankManager = PlayerTankManager.getInstance();
    protected final CoinManager coinManager = CoinManager.getInstance();
    protected final ScoreManipulation scoreManipulation = ScoreManipulation.INSTANCE;
    protected final EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List.of(obstacleGeneration, bulletFactory, coinManager,enemyTankManager,playerTankManager,scoreManipulation)
                .forEach(drawable -> drawable.draw(spriteBatch));
    }

    @Override
    public void update() {
     List.of(obstacleGeneration, bulletFactory, coinManager,enemyTankManager,playerTankManager,rTreeMap)
             .forEach(Updatable::update);
    }

}
