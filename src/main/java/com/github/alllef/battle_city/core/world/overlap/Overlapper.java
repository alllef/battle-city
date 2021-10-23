package com.github.alllef.battle_city.core.world.overlap;

import com.github.alllef.battle_city.core.game_entity.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;

import java.util.List;

public enum Overlapper {
    INSTANCE;
    protected final BulletFactory bulletFactory = BulletFactory.getInstance();
    protected final ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    protected final PlayerTankManager playerTank = PlayerTank.getInstance();
    protected final EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();

    public void overlaps(GameEntity firstEntity, GameEntity secondEntity) {
        List<GameEntity> entities = List.of(firstEntity, secondEntity);
        for (int i = 0; i < 2; i++) {
            firstEntity = entities.get(i % 2);
            secondEntity = entities.get((i + 1) % 2);

            if (firstEntity instanceof Bullet bullet && secondEntity instanceof Obstacle obstacle) {
                bulletFactory.shootObstacle(bullet);
                obstacleGeneration.bulletShoot(obstacle);
            } else if (firstEntity instanceof Bullet bullet && secondEntity instanceof EnemyTank enemyTank) {
                bulletFactory.shootTank(bullet);
                enemyTankManager.bulletShoot(enemyTank);
            } else if (firstEntity instanceof SingleTank singleTank && secondEntity instanceof Obstacle obstacle) {
                singleTank.overlapsObstacle(obstacle);
            } else if (firstEntity instanceof SingleTank firstTank && secondEntity instanceof SingleTank secondTank && firstTank != secondTank) {
                firstTank.overlapsTank();
                secondTank.overlapsTank();
            }
            else if (firstEntity instanceof Bullet bullet1 && secondEntity instanceof Bullet bullet2 && bullet1!=bullet2) {
                bulletFactory.shootBullet(bullet1,bullet2);
            }

        }
    }

}
