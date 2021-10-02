package com.github.alllef.battle_city.core.game_entity.tank.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.RTreeMap;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;
import rx.Observable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;

public class AIPlayerTank extends PlayerTank {
    private RTree<GameEntity, RectangleFloat> rTree = RTreeMap.getInstance().getrTree();
    private final int worldSize;
    GdxToRTreeRectangleMapper mapper = GdxToRTreeRectangleMapper.ENTITY;
    Queue<Coords> coordsToTarget = new LinkedList<>();

    protected AIPlayerTank(BulletFactory bulletFactory) {
        super(bulletFactory);
        worldSize = prefs.getInteger("world_size");
    }

    @Override
    public void shoot() {
        if (areTanksOnParallel())
            super.shoot();
    }

    @Override
    public void ride() {
        Coords turnCoord = getTurnCoord();
        if (this.getDir() == Direction.RIGHT) {
            while (this.getSprite().getX() < turnCoord.x())
                ride(this.getDir());
        }
        if (this.getDir() == Direction.RIGHT) {
            while (this.getSprite().getX() < turnCoord.x())
                ride(this.getDir());
        }
        if (this.getDir() == Direction.RIGHT) {
            while (this.getSprite().getX() < turnCoord.x())
                ride(this.getDir());
        }
        if (this.getDir() == Direction.RIGHT) {
            while (this.getSprite().getX() < turnCoord.x())
                ride(this.getDir());
        }
    }

    private Coords getTurnCoord() {
        Coords first = coordsToTarget.poll();
        Coords second = coordsToTarget.poll();
        BiPredicate<Coords, Coords> rightPredicate = (coord1, coord2) -> coord1.x() < coord2.x();
        BiPredicate<Coords, Coords> leftPredicate = (coord1, coord2) -> coord1.x() > coord2.x();
        BiPredicate<Coords, Coords> upPredicate = (coord1, coord2) -> coord1.y() < coord2.y();
        BiPredicate<Coords, Coords> downPredicate = (coord1, coord2) -> coord1.y() > coord2.y();

        if (rightPredicate.test(first, second)) {
            turnCoordCycle(rightPredicate, first, second);
            this.setDir(Direction.RIGHT);
        } else if (leftPredicate.test(first, second)) {
            turnCoordCycle(rightPredicate, first, second);
            this.setDir(Direction.LEFT);
        } else if (upPredicate.test(first, second)) {
            turnCoordCycle(upPredicate, first, second);
            this.setDir(Direction.UP);
        }

        else if (downPredicate.test(first, second)) {
            turnCoordCycle(upPredicate, first, second);
            this.setDir(Direction.DOWN);
        }

        while (second.x() > first.x()) {
            first = second;
            second = coordsToTarget.poll();
        }

        if (second.x() < first.x())
            while (second.x() < first.x()) {
                this.setDir(Direction.LEFT);

                first = second;
                second = coordsToTarget.poll();
            }

        if (second.y() > first.y())
            while (second.y() > first.y()) {
                this.setDir(Direction.UP);
                first = second;
                second = coordsToTarget.poll();
            }

        if (second.y() < first.y())
            while (second.y() < first.y()) {
                this.setDir(Direction.DOWN);
                first = second;
                second = coordsToTarget.poll();
            }

        return first;
    }

    private void turnCoordCycle(BiPredicate<Coords, Coords> predicate, Coords first, Coords second) {
        while (predicate.test(first, second)) {
            first = second;
            second = coordsToTarget.poll();
        }
    }

    private Coords getRandomNonObstacleCoord() {
        Random random = new Random();
        SpriteParam tankParam = SpriteParam.PLAYER_TANK;
        int rightBounds = (int) (worldSize - tankParam.getWidth());
        int upperBounds = (int) (worldSize - tankParam.getHeight());
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

    private boolean areTanksOnParallel() {
        Rectangle rect = this.getSprite().getBoundingRectangle();
        Observable<Entry<GameEntity, RectangleFloat>> obstacleList = null;
        switch (this.getDir()) {
            case UP -> obstacleList = rTree.search(Geometries.rectangle(rect.getX(), rect.getY() + SpriteParam.PLAYER_TANK.getHeight(), rect.getX(), worldSize));
            case DOWN -> obstacleList = rTree.search(Geometries.rectangle(rect.getX(), rect.getY() - SpriteParam.PLAYER_TANK.getHeight(), rect.getX(), 0));
            case RIGHT -> obstacleList = rTree.search(Geometries.rectangle(rect.getX() + SpriteParam.PLAYER_TANK.getHeight(), rect.getY(), worldSize, rect.getY()));
            case LEFT -> obstacleList = rTree.search(Geometries.rectangle(rect.getX(), rect.getY() + SpriteParam.PLAYER_TANK.getHeight(), 0, rect.getY()));
        }

        Iterator<Entry<GameEntity, RectangleFloat>> iterator = obstacleList.toBlocking().getIterator();

        while (iterator.hasNext()) {
            Entry<GameEntity, RectangleFloat> entry = iterator.next();
            if (entry.value() instanceof EnemyTank)
                return true;
        }

        return false;
    }
}
