package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.util.Drawable;

import java.util.List;

public abstract class WorldMap implements Drawable {

    BulletFactory bulletFactory = BulletFactory.INSTANCE;
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();

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
}
