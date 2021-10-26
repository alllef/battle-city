package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.RectUtils;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.overlap.Overlapper;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;

import java.util.*;

public class RTreeMap {

    private final GdxToRTreeRectangleMapper rectangleMapper = GdxToRTreeRectangleMapper.ENTITY;
    private final Overlapper overlapper = Overlapper.INSTANCE;
    private final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    private RTree<GameEntity, RectangleFloat> worldRTree = RTree.create();
    private RTree<GameEntity, RectangleFloat> coinRTree = RTree.create();

    public void createRtree(List<GameEntity> entities) {
        List<Entry<GameEntity, RectangleFloat>> entryList = new ArrayList<>();
        entities.forEach(gameEntity -> entryList.add(getEntry(gameEntity)));
        worldRTree = RTree.create(entryList);
    }

    public void delete(GameEntity entity) {
        worldRTree = worldRTree.delete(entity, rectangleMapper.convertToRTreeRectangle(entity.getRect()));
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

    protected void checkOverlappings() {
        worldRTree.entries().forEach(this::checkOverlapping);
    }

    private void checkOverlapping(Entry<GameEntity, RectangleFloat> entry) {
        var overlappingEntities = worldRTree.search(entry.geometry());
        overlappingEntities.forEach(tmpEntity -> overlapper.overlaps(tmpEntity.value(), entry.value()));
    }

    public Optional<Iterator<Entry<GameEntity, RectangleFloat>>> getParallelObstacles(Direction dir, Coords coords) {
        RectangleFloat treeRect = null;
        SpriteParam param = SpriteParam.PLAYER_TANK;

        switch (dir) {
            case UP -> treeRect = RectUtils.getFloatRect(coords.x(), coords.y() + param.getHeight(), coords.x() + 3, prefs.getInteger("world_size"));
            case DOWN -> treeRect = RectUtils.getFloatRect(0, coords.x(), coords.y(), coords.x() + 3);
            case RIGHT -> treeRect = RectUtils.getFloatRect(coords.x() + param.getWidth(), coords.y(), prefs.getInteger("world_size"), coords.y() + 3);
            case LEFT -> treeRect = RectUtils.getFloatRect(0, coords.y(), coords.x(), coords.y() + 3);
        }

        if (treeRect.y1() < 0 || treeRect.x1() < 0 || treeRect.x2() < 0 || treeRect.y2() < 0)
            return Optional.empty();

        return Optional.of(worldRTree.search(treeRect)
                .toBlocking()
                .getIterator());
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
