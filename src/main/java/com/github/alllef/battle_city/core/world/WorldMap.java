package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.util.Drawable;

import java.util.List;

public abstract class WorldMap implements Drawable {
    protected BulletFactory bulletFactory = BulletFactory.INSTANCE;
    protected EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    protected ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();
    protected final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List.of(obstacleGeneration, playerTank, enemyTankManager, bulletFactory)
                .forEach(drawable -> drawable.draw(spriteBatch));
    }

    protected Array<GameEntity> getEntitiesArray() {
        Array<GameEntity> entitiesArray = new Array<>();
        entitiesArray.addAll(bulletFactory.getBullets());
        entitiesArray.addAll(enemyTankManager.getEnemyTanks());
        entitiesArray.add(playerTank);
        entitiesArray.addAll(obstacleGeneration.getObstacles());
        return entitiesArray;
    }

    public abstract void update();
}
