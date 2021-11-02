package com.github.alllef.battle_city.core.game_entity.tank.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.path_algorithm.AlgoType;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab2.AStarAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.RectUtils;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.RTreeMap;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiPredicate;

public class AIPlayerTankWrapper extends PlayerTankManager {
    RTreeMap rTreeMap;
    Stack<Coords> coordsToTarget = new Stack<>();
    private Coords turnCoord = null;
    private GdxToRTreeRectangleMapper mapper = GdxToRTreeRectangleMapper.ENTITY;

    public AIPlayerTankWrapper(RTreeMap rTreeMap, BulletFactory bulletFactory, Preferences prefs) {
        super(bulletFactory, prefs);
        this.rTreeMap = rTreeMap;
        generatePath();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        playerTank.draw(spriteBatch);

        ShapeDrawer shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(new Texture(Gdx.files.internal("sprites/block.png"))));
        shapeDrawer.setColor(Color.YELLOW);
        coordsToTarget.forEach(coords ->
                shapeDrawer.line(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1));
    }

    @Override
    public void shoot() {
        if (areObstaclesOnParallel())
            playerTank.shoot();
    }

    @Override
    public void ride(Direction dir) {
        if (turnCoord == null)
            turnCoord = getTurnCoord();

        Coords tankCoord = new Coords((int) this.getSprite().getX(), (int) this.getSprite().getY());

        Optional<Map.Entry<BiPredicate<Coords, Coords>, Direction>> predicateEntry = getPredicateMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(this.getDir()))
                .findFirst();

        BiPredicate<Coords, Coords> predicate = null;

        if (predicateEntry.isPresent())
            predicate = predicateEntry.get().getKey();

        if (predicate.test(tankCoord, turnCoord)) {
            setRideLooping(true);
            playerTank.ride(this.getDir());
        } else
            turnCoord = getTurnCoord();
    }


    private Coords getTurnCoord() {
        Coords first = coordsToTarget.peek();
        coordsToTarget.pop();

        if (coordsToTarget.isEmpty()) {
            generatePath();
            getTurnCoord();
        }
        Coords second = coordsToTarget.peek();

        var predicateMap = getPredicateMap();

        for (var predicate : predicateMap.keySet()) {
            Direction dir = predicateMap.get(predicate);
            if (predicate.test(first, second) && playerTank.getBlockedDirection() != dir) {
                playerTank.ride(dir);
                break;
            }
        }

        return first;
    }

    private void generatePath() {

        Coords coords = rTreeMap.getRandomNonObstacleCoord(SpriteParam.PLAYER_TANK);
        Rectangle coordsRect = mapper.convertToGdxRectangle(RectUtils.getSmallestRect(coords));
        AStarAlgo algo = new AStarAlgo(rTreeMap, playerTank.getRect(), coordsRect, AlgoType.ASTAR_COORDS, prefs);
        coordsToTarget.addAll(algo.startAlgo());

    }

    private Map<BiPredicate<Coords, Coords>, Direction> getPredicateMap() {
        Map<BiPredicate<Coords, Coords>, Direction> predicateMap = new HashMap<>();

        predicateMap.put((coord1, coord2) -> coord1.x() < coord2.x(), Direction.RIGHT);
        predicateMap.put((coord1, coord2) -> coord1.x() > coord2.x(), Direction.LEFT);
        predicateMap.put((coord1, coord2) -> coord1.y() < coord2.y(), Direction.UP);
        predicateMap.put((coord1, coord2) -> coord1.y() > coord2.y(), Direction.DOWN);

        return predicateMap;
    }

    private boolean areObstaclesOnParallel() {
        Sprite tankSprite = getSprite();
        Coords coords = new Coords((int) tankSprite.getX(), (int) tankSprite.getY());
        var optionalIterator = rTreeMap.getParallelObstacles(getDir(), coords);

        if (optionalIterator.isPresent()) {
            var iter = optionalIterator.get();
            while (iter.hasNext()) {
                var entry = iter.next();
                //if (entry.value() instanceof EnemyTank)
                    return true;
            }
        }
        return false;
    }

    @Override
    public void update() {
        shoot();
        ride(playerTank.getDir());
    }
}
