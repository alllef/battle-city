package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;

public class RtreeMatrix {
    RTree<Obstacle, RectangleFloat> rTree;

    BulletFactory bulletFactory = BulletFactory.INSTANCE;
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();

    public void createRTree() {

    }

    private Array<GameEntity> getMovingEntitiesArray() {
        Array<GameEntity> entitiesArray = new Array<>();
        entitiesArray.addAll(bulletFactory.getBullets());
        entitiesArray.addAll(enemyTankManager.getEnemyTanks());
        entitiesArray.add(playerTank);
        return entitiesArray;
    }
}
