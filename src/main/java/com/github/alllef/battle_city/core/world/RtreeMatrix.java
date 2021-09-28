package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class RtreeMatrix implements Drawable {
    RTree<GameEntity, RectangleFloat> rTree;
    GdxToRTreeRectangleMapper rectangleMapper = new GdxToRTreeRectangleMapper();

    BulletFactory bulletFactory = BulletFactory.INSTANCE;
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();

    public RtreeMatrix() {
        createRtree();
    }

    private Array<GameEntity> getEntitiesArray() {
        Array<GameEntity> entitiesArray = new Array<>();
        entitiesArray.addAll(bulletFactory.getBullets());
        entitiesArray.addAll(enemyTankManager.getEnemyTanks());
        entitiesArray.add(playerTank);
        entitiesArray.addAll(obstacleGeneration.getObstacles());
        return entitiesArray;
    }

    Entry<GameEntity, RectangleFloat> getEntry(GameEntity gameEntity) {
        Rectangle gdxRectangle = gameEntity.getSprite().getBoundingRectangle();
        RectangleFloat rTreeRectangle = rectangleMapper.convertToRTreeRectangle(gdxRectangle);
        return new Entry<>() {
            @Override
            public GameEntity value() {
                return gameEntity;
            }

            @Override
            public RectangleFloat geometry() {
                return rTreeRectangle;
            }
        };
    }

    public void createRtree() {
        List<Entry<GameEntity, RectangleFloat>> entryList = new ArrayList<>();

        getEntitiesArray().forEach(gameEntity -> entryList.add(getEntry(gameEntity)));
        rTree = RTree.create(entryList);
    }

    public void updateRtree() {
        bulletFactory.updateBullets();
        enemyTankManager.ride();
        enemyTankManager.shoot();
        playerTank.ride();
        getEntitiesArray().forEach(this::checkOverlapping);
        createRtree();
    }

    private void checkOverlapping(GameEntity gameEntity) {

        Observable<Entry<GameEntity, RectangleFloat>> overlappingEntities = rTree.search(getEntry(gameEntity).geometry());
        overlappingEntities.forEach(tmpEntity -> checkOverlaps(gameEntity, tmpEntity.value()));
    }

    public void checkOverlaps(GameEntity firstEntity, GameEntity secondEntity) {
        if (firstEntity instanceof Bullet bullet && secondEntity instanceof Obstacle obstacle) {
            checkBulletShootObstacle(bullet, obstacle);
        } else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof Bullet bullet) {
            checkBulletShootObstacle(bullet, obstacle);
        } else if (firstEntity instanceof Bullet bullet && secondEntity instanceof EnemyTank enemyTank) {
            checkBulletShootTank(bullet, enemyTank);
        } else if (firstEntity instanceof EnemyTank enemyTank && secondEntity instanceof Bullet bullet) {
            checkBulletShootTank(bullet, enemyTank);
        } else if (firstEntity instanceof SingleTank singleTank && secondEntity instanceof Obstacle obstacle) {
            singleTank.checkOverlapsObstacle(obstacle);
        } else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof SingleTank singleTank) {
            singleTank.checkOverlapsObstacle(obstacle);
        } else if (firstEntity instanceof SingleTank singleTank && secondEntity instanceof SingleTank singleTank1 && singleTank != singleTank1) {
            checkOverlapsTank(singleTank, singleTank1);
        } else if (firstEntity instanceof Bullet bullet && secondEntity instanceof Bullet bullet1 && bullet != bullet1) {
            checkBulletOverlapping(bullet, bullet1);
        } /*else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof Obstacle obstacle1 && obstacle != obstacle1) {
            throw new IndexOutOfBoundsException();
        }*/

    }

    public void checkOverlapsTank(SingleTank singleTank, SingleTank secondTank) {

        if (singleTank.getSprite().getBoundingRectangle().overlaps(secondTank.getSprite().getBoundingRectangle()))
            List.of(singleTank, secondTank).forEach(this::tankOverlapTank);

    }

    public void tankOverlapTank(SingleTank singleTank) {
        System.out.println("wtf");
        singleTank.setBlockedDirection(singleTank.getDir());
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        float minChangeDistance = prefs.getFloat("min_change_distance");
        switch (singleTank.getDir()) {
            case UP -> singleTank.getSprite().setY(singleTank.getSprite().getY() - minChangeDistance);
            case DOWN -> singleTank.getSprite().setY(singleTank.getSprite().getY() + minChangeDistance);
            case LEFT -> singleTank.getSprite().setX(singleTank.getSprite().getX() + minChangeDistance);
            case RIGHT -> singleTank.getSprite().setX(singleTank.getSprite().getX() - minChangeDistance);
        }

    }

    public void checkBulletShootTank(Bullet bullet, EnemyTank enemyTank) {
        if (bullet.getSprite().getBoundingRectangle().overlaps(enemyTank.getSprite().getBoundingRectangle())) {
            enemyTankManager.getEnemyTanks().removeValue(enemyTank, true);
            bulletFactory.getBullets().removeValue(bullet, true);
            rTree = rTree.delete(bullet, rectangleMapper.convertToRTreeRectangle(bullet.getSprite().getBoundingRectangle()));
            rTree = rTree.delete(enemyTank, rectangleMapper.convertToRTreeRectangle(bullet.getSprite().getBoundingRectangle()));

            //                score += prefs.getInteger("killed_tank_score");
        }
    }

    public void checkBulletOverlapping(Bullet firstBullet, Bullet secondBullet) {
        if (firstBullet.getSprite().getBoundingRectangle().overlaps(secondBullet.getSprite().getBoundingRectangle())) {
            bulletFactory.getBullets().removeValue(firstBullet, true);
            bulletFactory.getBullets().removeValue(secondBullet, true);
            rTree = rTree.delete(firstBullet, rectangleMapper.convertToRTreeRectangle(firstBullet.getSprite().getBoundingRectangle()));
            rTree = rTree.delete(secondBullet, rectangleMapper.convertToRTreeRectangle(secondBullet.getSprite().getBoundingRectangle()));
        }
    }

    public void checkBulletShootObstacle(Bullet bullet, Obstacle obstacle) {
        Array<Bullet> bullets = bulletFactory.getBullets();
        if (bullet.getSprite().getBoundingRectangle().overlaps(obstacle.getSprite().getBoundingRectangle())) {
            obstacleGeneration.getObstacles().removeValue(obstacle, true);
            bullets.removeValue(bullet, true);

            rTree = rTree.delete(bullet, rectangleMapper.convertToRTreeRectangle(bullet.getSprite().getBoundingRectangle()));
            rTree = rTree.delete(obstacle, rectangleMapper.convertToRTreeRectangle(obstacle.getSprite().getBoundingRectangle()));
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List.of(obstacleGeneration, playerTank, enemyTankManager, bulletFactory)
                .forEach(drawable -> drawable.draw(spriteBatch));
    }

}
