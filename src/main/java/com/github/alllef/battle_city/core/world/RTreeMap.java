package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;
import rx.Observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RTreeMap extends WorldMap {
    private static final RTreeMap rTreeMap = new RTreeMap();

    public static RTreeMap getInstance() {
        return rTreeMap;
    }

    RTree<GameEntity, RectangleFloat> rTree;
    GdxToRTreeRectangleMapper rectangleMapper = GdxToRTreeRectangleMapper.ENTITY;

    private RTreeMap() {
        createRtree();
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
        }

    }

    public void checkOverlapsTank(SingleTank singleTank, SingleTank secondTank) {

        if (singleTank.getSprite().getBoundingRectangle().overlaps(secondTank.getSprite().getBoundingRectangle()))
            List.of(singleTank, secondTank).forEach(this::tankOverlapTank);

    }

    public void tankOverlapTank(SingleTank singleTank) {
        singleTank.setBlockedDirection(singleTank.getDir());
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        float minChangeDistance = prefs.getFloat("min_change_distance");
        Sprite tankSprite = singleTank.getSprite();

        switch (singleTank.getDir()) {
            case UP -> singleTank.getSprite().setY(tankSprite.getY() - minChangeDistance);
            case DOWN -> singleTank.getSprite().setY(tankSprite.getY() + minChangeDistance);
            case LEFT -> singleTank.getSprite().setX(tankSprite.getX() + minChangeDistance);
            case RIGHT -> singleTank.getSprite().setX(tankSprite.getX() - minChangeDistance);
        }

    }

    public void checkBulletShootTank(Bullet bullet, EnemyTank enemyTank) {
        if (bullet.getSprite().getBoundingRectangle().overlaps(enemyTank.getSprite().getBoundingRectangle())) {
            enemyTankManager.getEnemyTanks().removeValue(enemyTank, true);
            bulletFactory.getBullets().removeValue(bullet, true);
            //                score += prefs.getInteger("killed_tank_score");
        }
    }

    public void checkBulletOverlapping(Bullet firstBullet, Bullet secondBullet) {
        if (firstBullet.getSprite().getBoundingRectangle().overlaps(secondBullet.getSprite().getBoundingRectangle()))
            List.of(firstBullet, secondBullet).forEach(bullet ->
                    bulletFactory.getBullets().removeValue(firstBullet, true));
    }

    public void checkBulletShootObstacle(Bullet bullet, Obstacle obstacle) {
        Array<Bullet> bullets = bulletFactory.getBullets();
        if (bullet.getSprite().getBoundingRectangle().overlaps(obstacle.getSprite().getBoundingRectangle())) {
            obstacleGeneration.getObstacles().removeValue(obstacle, true);
            bullets.removeValue(bullet, true);
        }
    }

    @Override
    public void update() {
        getEntitiesArray().forEach(this::checkOverlapping);
        createRtree();
    }

    public Iterator<Entry<GameEntity, RectangleFloat>> getParallelObstacles(Direction dir, Coords coords){
        Observable<Entry<GameEntity, RectangleFloat>> obstacleList = null;
        switch (dir) {
            case UP -> obstacleList = rTree.search(Geometries.rectangle(coords.x(), coords.y() + SpriteParam.PLAYER_TANK.getHeight(), coords.x(), prefs.getInteger("world_size")));
            case DOWN -> obstacleList = rTree.search(Geometries.rectangle(coords.x(), coords.y() - SpriteParam.PLAYER_TANK.getHeight(), coords.x(), 0));
            case RIGHT -> obstacleList = rTree.search(Geometries.rectangle(coords.x() + SpriteParam.PLAYER_TANK.getHeight(), coords.y(), prefs.getInteger("world_size"), coords.y()));
            case LEFT -> obstacleList = rTree.search(Geometries.rectangle(coords.x(), coords.y() + SpriteParam.PLAYER_TANK.getHeight(), 0, coords.y()));
        }
        return obstacleList.toBlocking().getIterator();
    }

    public Coords getRandomNonObstacleCoord() {
        Random random = new Random();
        SpriteParam tankParam = SpriteParam.PLAYER_TANK;
        int rightBounds = (int) (prefs.getInteger("world_size") - tankParam.getWidth());
        int upperBounds = (int) (prefs.getInteger("world_size") - tankParam.getHeight());
        int x;
        int y;

        while (true) {
            x = random.nextInt(rightBounds);
            y = random.nextInt(upperBounds);
            RectangleFloat floatRect = (RectangleFloat) Geometries.rectangle(x, y, x + tankParam.getWidth(), y + tankParam.getHeight());

            Observable<Entry<GameEntity, RectangleFloat>> tmpList = rTree.search(floatRect);

            if (tmpList.isEmpty().toBlocking().first())
                break;
        }

        return new Coords(x, y);
    }

    public RTree<GameEntity, RectangleFloat> getrTree() {
        return rTree;
    }
}
