package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.obstacle.ObstacleGeneration;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.ReflexEnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;
import com.github.alllef.battle_city.core.util.*;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;
import com.github.alllef.battle_city.core.util.interfaces.Updatable;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.overlap.Overlapper;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;
import rx.Observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RTreeMap implements Updatable {
    private static RTreeMap rTreeMap;

    public static RTreeMap getInstance() {
        if (rTreeMap == null)
            rTreeMap = new RTreeMap();

        return rTreeMap;
    }

    private final GdxToRTreeRectangleMapper rectangleMapper = GdxToRTreeRectangleMapper.ENTITY;
    private final Overlapper overlapper = Overlapper.INSTANCE;
    private final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    private RTree<GameEntity, RectangleFloat> worldRTree = RTree.create();
    private RTree<GameEntity, RectangleFloat> coinRTree = RTree.create();

    protected final BulletFactory bulletFactory = BulletFactory.getInstance();
    protected final ObstacleGeneration obstacleGeneration = ObstacleGeneration.getInstance();
    protected final PlayerTankManager playerTankManager = PlayerTankManager.getInstance();
    protected ReflexEnemyTankManager enemyTankManager;

    private RTreeMap() {
        createRtree();
    }

    protected Array<GameEntity> getEntitiesArray() {
        Array<GameEntity> entitiesArray = new Array<>();
        entitiesArray.addAll(bulletFactory.getEntities());

        if (enemyTankManager == null)
            enemyTankManager = ReflexEnemyTankManager.getInstance();

        entitiesArray.addAll(enemyTankManager.getEntities());
        entitiesArray.addAll(playerTankManager.getEntities());
        entitiesArray.addAll(obstacleGeneration.getEntities());
        return entitiesArray;
    }

    public void createRtree() {
        List<Entry<GameEntity, RectangleFloat>> entryList = new ArrayList<>();
        getEntitiesArray().forEach(gameEntity -> entryList.add(getEntry(gameEntity)));
        worldRTree = RTree.create(entryList);
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

    public int getRtreeSize() {
        return worldRTree.size();
    }

    public void addEntities(List<GameEntity> entities) {
        List<Entry<GameEntity, RectangleFloat>> entryList = new ArrayList<>();
        entities.forEach(gameEntity -> entryList.add(getEntry(gameEntity)));
        worldRTree = worldRTree.add(entryList);
        System.out.println(worldRTree.size());
    }

    public boolean isEmpty(Coords coords) {
        return isEmpty(worldRTree, RectUtils.getSmallestRect(coords));
    }

    public boolean hasCoins(Coords coords) {
        return isEmpty(coinRTree, RectUtils.getSmallestRect(coords));
    }

    private boolean isEmpty(RTree<GameEntity, RectangleFloat> rTree, RectangleFloat gdxRect) {
        return rTree.search(gdxRect)
                .isEmpty()
                .toBlocking()
                .first();
    }


    private void checkOverlapping(Entry<GameEntity, RectangleFloat> entry) {
        var overlappingEntities = worldRTree.search(entry.geometry());
        overlappingEntities.forEach(tmpEntity -> overlapper.overlaps(tmpEntity.value(), entry.value()));
    }

    @Override
    public void update() {
        worldRTree.entries().forEach(this::checkOverlapping);
        createRtree();
    }

    public Iterator<Entry<GameEntity, RectangleFloat>> getParallelObstacles(Direction dir, Coords coords) {
        RectangleFloat treeRect = null;
        SpriteParam param = SpriteParam.PLAYER_TANK;

        switch (dir) {
            case UP -> treeRect = RectUtils.getFloatRect(coords.x(), coords.y() + param.getHeight(), coords.x(), prefs.getInteger("world_size"));
            case DOWN -> treeRect = RectUtils.getFloatRect(0, coords.x(), coords.y(), coords.x());
            case RIGHT -> treeRect = RectUtils.getFloatRect(coords.x() + param.getWidth(), coords.y(), prefs.getInteger("world_size"), coords.y());
            case LEFT -> treeRect = RectUtils.getFloatRect(0, coords.y(), coords.x(), coords.y());
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

        return worldRTree.search(treeRect)
                .toBlocking()
                .getIterator();
    }

    public Coords getRandomNonObstacleCoord(SpriteParam param) {
        Random random = new Random();
        int rightBounds = (int) (prefs.getInteger("world_size") - param.getWidth());
        int upperBounds = (int) (prefs.getInteger("world_size") - param.getHeight());
        int x;
        int y;

        while (true) {
            x = random.nextInt(rightBounds);
            y = random.nextInt(upperBounds);
            RectangleFloat floatRect = RectUtils.getFloatRect(x, y, x + param.getWidth(), y + param.getHeight());

            if (isEmpty(worldRTree, floatRect))
                break;
        }

        return new Coords(x, y);
    }
}
