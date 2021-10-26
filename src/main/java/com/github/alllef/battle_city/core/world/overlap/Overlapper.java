package com.github.alllef.battle_city.core.world.overlap;

import com.github.alllef.battle_city.core.game_entity.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.ReflexEnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.ReflexEnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.util.enums.GameResult;
import com.github.alllef.battle_city.core.world.stats.GameStats;

public class Overlapper {

    private final BulletFactory bulletFactory;
    private final ObstacleGeneration obstacleGeneration;
    private final ReflexEnemyTankManager enemyTankManager;
    private final GameStats stats;

    public Overlapper(BulletFactory bulletFactory, ObstacleGeneration obstacleGeneration, ReflexEnemyTankManager enemyTankManager, GameStats stats) {
        this.bulletFactory = bulletFactory;
        this.obstacleGeneration = obstacleGeneration;
        this.enemyTankManager = enemyTankManager;
        this.stats = stats;
    }

    public void overlaps(GameEntity firstEntity, GameEntity secondEntity) {

        if (firstEntity instanceof Bullet bullet && secondEntity instanceof Obstacle obstacle) {
            bulletFactory.shootObstacle(bullet);
            obstacleGeneration.bulletShoot(obstacle);
        } else if (firstEntity instanceof Bullet bullet && secondEntity instanceof ReflexEnemyTank enemyTank) {
            bulletFactory.shootTank(bullet);
            enemyTankManager.bulletShoot(enemyTank);
            if (enemyTankManager.getEntities().isEmpty())
                stats.setGameOver(GameResult.WIN);
        }
        else if (firstEntity instanceof PlayerTank tank && secondEntity instanceof Bullet bullet) {
            stats.setGameOver(GameResult.LOSE);
        }

        else if (firstEntity instanceof SingleTank singleTank && secondEntity instanceof Obstacle obstacle) {
            singleTank.overlapsObstacle();
        } else if (firstEntity instanceof SingleTank firstTank && secondEntity instanceof SingleTank secondTank && firstTank != secondTank) {
            firstTank.overlapsTank();
            secondTank.overlapsTank();
        } else if (firstEntity instanceof Bullet bullet1 && secondEntity instanceof Bullet bullet2 && bullet1 != bullet2) {
            bulletFactory.shootBullet(bullet1, bullet2);
        }

    }
}