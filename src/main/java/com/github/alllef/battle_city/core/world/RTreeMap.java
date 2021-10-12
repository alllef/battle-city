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

import java.util.*;

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

    public boolean isEmpty(Coords coords) {
        return rTree.search(getSmallestRect(coords))
                .isEmpty()
                .toBlocking()
                .first();
    }

    public RectangleFloat getSmallestRect(Coords coords) {
        return (RectangleFloat) Geometries.rectangle(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1);
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

    public Iterator<Entry<GameEntity, RectangleFloat>> getParallelObstacles(Direction dir, Coords coords) {
        RectangleFloat treeRect = null;

        switch (dir) {
            case UP -> treeRect = (RectangleFloat) Geometries.rectangle(coords.x(), coords.y() + SpriteParam.PLAYER_TANK.getHeight(), coords.x(), prefs.getInteger("world_size"));
            case DOWN -> treeRect = (RectangleFloat) Geometries.rectangle(0, coords.x(), coords.y(), coords.x());
            case RIGHT -> treeRect = (RectangleFloat) Geometries.rectangle(coords.x() + SpriteParam.PLAYER_TANK.getWidth(), coords.y(), prefs.getInteger("world_size"), coords.y());
            case LEFT -> treeRect = (RectangleFloat) Geometries.rectangle(0, coords.y(), coords.x(), coords.y());
        }

        if (treeRect.y1() < 0 || treeRect.x1() < 0 || treeRect.x2() < 0 || treeRect.y2() < 0)
            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Entry<GameEntity, RectangleFloat> next() {
                    return null;
                }
            };

        return rTree.search(treeRect).toBlocking().getIterator();
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
