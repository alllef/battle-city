package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.bullet.Bullet;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.obstacle.Obstacle;
import com.github.alllef.battle_city.core.game_entity.tank.SingleTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.overlap.Overlapper;
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

    RTree<GameEntity, RectangleFloat> worldRTree = RTree.create();
    RTree<GameEntity, RectangleFloat> coinRTree;
    GdxToRTreeRectangleMapper rectangleMapper = GdxToRTreeRectangleMapper.ENTITY;
    Overlapper overlapper = Overlapper.INSTANCE;

    private RTreeMap() {
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

    public void addEntities(List<GameEntity> entities) {
        List<Entry<GameEntity, RectangleFloat>> entryList = new ArrayList<>();
        entities.forEach(gameEntity -> entryList.add(getEntry(gameEntity)));
        worldRTree = worldRTree.add(entryList);
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
        overlappingEntities.forEach(tmpEntity -> overlapper.overlaps(tmpEntity.value(), gameEntity));
    }

    @Override
    public void update() {
        getEntitiesArray().forEach(this::checkOverlapping);
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
