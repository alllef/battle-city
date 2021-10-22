package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.PlayerReflexEnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.ReflexEnemyTank;
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

    RTree<GameEntity, RectangleFloat> worldRTree;
    RTree<GameEntity, RectangleFloat> coinRTree;
    GdxToRTreeRectangleMapper rectangleMapper = GdxToRTreeRectangleMapper.ENTITY;

    private RTreeMap() {
        createRtree();
        createCoinRtree();
    }

    Entry<GameEntity, RectangleFloat> getEntry(GameEntity gameEntity) {
        Rectangle gdxRectangle = gameEntity.getRect();
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

    public void createCoinRtree() {
        List<Entry<GameEntity, RectangleFloat>> entryList = new ArrayList<>();
        coinManager.getCoinArr().forEach(gameEntity -> entryList.add(getEntry(gameEntity)));
        coinRTree = RTree.create(entryList);
    }

    public void createRtree() {
        List<Entry<GameEntity, RectangleFloat>> entryList = new ArrayList<>();
        getEntitiesArray().forEach(gameEntity -> entryList.add(getEntry(gameEntity)));
        worldRTree = RTree.create(entryList);
    }

    public boolean isEmpty(Coords coords) {
        return isEmpty(worldRTree, getSmallestRect(coords));
    }

    public boolean hasCoins(Coords coords) {
        return isEmpty(coinRTree, getSmallestRect(coords));
    }

    private boolean isEmpty(RTree<GameEntity, RectangleFloat> rTree, RectangleFloat gdxRect) {
        return rTree.search(gdxRect)
                .isEmpty()
                .toBlocking()
                .first();
    }

    public RectangleFloat getSmallestRect(Coords coords) {
        return (RectangleFloat) Geometries.rectangle(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1);
    }

    private void checkOverlapping(GameEntity gameEntity) {
        Observable<Entry<GameEntity, RectangleFloat>> overlappingEntities = worldRTree.search(getEntry(gameEntity).geometry());
        overlappingEntities.forEach(tmpEntity -> checkOverlaps(gameEntity, tmpEntity.value()));
    }

    public void checkOverlaps(GameEntity firstEntity, GameEntity secondEntity) {
        if (firstEntity instanceof Bullet bullet && secondEntity instanceof Obstacle obstacle) {
            checkBulletShootObstacle(bullet, obstacle);
        } else if (firstEntity instanceof Obstacle obstacle && secondEntity instanceof Bullet bullet) {
            checkBulletShootObstacle(bullet, obstacle);
        } else if (firstEntity instanceof Bullet bullet && secondEntity instanceof PlayerReflexEnemyTank enemyTank) {
            checkBulletShootTank(bullet, enemyTank);
            System.out.println("shoot bullet");
        } else if (firstEntity instanceof PlayerReflexEnemyTank enemyTank && secondEntity instanceof Bullet bullet) {
            checkBulletShootTank(bullet, enemyTank);
            System.out.println("shoot bullet");
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
        float minChangeDistance = prefs.getFloat("min_change_distance");
        Sprite tankSprite = singleTank.getSprite();

        switch (singleTank.getDir()) {
            case UP -> singleTank.getSprite().setY(tankSprite.getY() - minChangeDistance);
            case DOWN -> singleTank.getSprite().setY(tankSprite.getY() + minChangeDistance);
            case LEFT -> singleTank.getSprite().setX(tankSprite.getX() + minChangeDistance);
            case RIGHT -> singleTank.getSprite().setX(tankSprite.getX() - minChangeDistance);
        }

    }

    public void checkBulletShootTank(Bullet bullet, SingleTank enemyTank) {

        if (bullet.getRect().overlaps(enemyTank.getRect())) {
            System.out.println("Shoot");
            if (enemyTank instanceof ReflexEnemyTank reflex) {
                boolean removed = enemyTankManager.getEntities().removeValue(reflex, false);
                if (removed) {
                    System.out.println("Bullet removed");

                }
            }
            bulletFactory.getEntities().removeValue(bullet, true);
            scoreManipulation.tankKilled();
        }
    }

    public void checkBulletOverlapping(Bullet firstBullet, Bullet secondBullet) {
        if (firstBullet.getSprite().getBoundingRectangle().overlaps(secondBullet.getSprite().getBoundingRectangle()))
            List.of(firstBullet, secondBullet).forEach(bullet ->
                    bulletFactory.getEntities().removeValue(firstBullet, true));
    }

    public void checkBulletShootObstacle(Bullet bullet, Obstacle obstacle) {
        Array<Bullet> bullets = bulletFactory.getEntities();
        if (bullet.getSprite().getBoundingRectangle().overlaps(obstacle.getSprite().getBoundingRectangle())) {
            obstacleGeneration.getEntities().removeValue(obstacle, true);
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

        return worldRTree.search(treeRect).toBlocking().getIterator();
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

            if (isEmpty(worldRTree, floatRect))
                break;
        }

        return new Coords(x, y);
    }
}
