package com.github.alllef.battle_city.core.game_entity.tank.enemy.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos.UCSAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.world.RTreeMap;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.*;
import java.util.function.BiPredicate;

public class AIEnemyTankWrapper {
   /* private static final AIEnemyTankWrapper aiEnemyTankWrapper = new AIEnemyTankWrapper();

    public static final AIEnemyTankWrapper getInstance() {
        return aiEnemyTankWrapper;
    }

    private EnemyTank enemyTank;
    RTreeMap rTreeMap = RTreeMap.getInstance();
    Map<EnemyTank, Stack<Coords>> coordsToTargetMap = new HashMap<>();
    private Map<EnemyTank, Coords> turnCoordMap = new HashMap<>();
    protected final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    private EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    private PlayerTank playerTank = PlayerTank.getInstance();

    public AIEnemyTankWrapper() {
        enemyTankManager.getEntities().forEach(enemyTank1 -> {
            coordsToTargetMap.put(enemyTank1, new Stack<>());
            turnCoordMap.put(enemyTank1, null);
        });

    }

    public void draw(SpriteBatch spriteBatch) {
        Texture tmpTexture = new Texture(Gdx.files.internal("sprites/block.png"));
        ShapeDrawer shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(tmpTexture));
        shapeDrawer.setColor(Color.YELLOW);

        coordsToTargetMap.entrySet().forEach(stack -> {
            stack.getValue().forEach(coords ->
                    shapeDrawer.line(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1));
            stack.getKey().draw(spriteBatch);
        });
    }

    public void shoot() {
        enemyTankManager.getEntities().forEach(this::ride);
    }

    public void shoot(EnemyTank enemyTank) {
        this.enemyTank = enemyTank;
        if (areTanksOnParallel())
            enemyTank.shoot();
    }

    public void ride() {
        enemyTankManager.getEntities().forEach(this::ride);
    }

    public void ride(EnemyTank enemyTank) {

        this.enemyTank = enemyTank;
        if (turnCoordMap.get(enemyTank) == null)
            turnCoordMap.put(enemyTank, getTurnCoord());

        Coords tankCoord = new Coords((int) enemyTank.getSprite().getX(), (int) enemyTank.getSprite().getY());

        Optional<Map.Entry<BiPredicate<Coords, Coords>, Direction>> predicateEntry = getPredicateMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(enemyTank.getDir()))
                .findFirst();

        BiPredicate<Coords, Coords> predicate = null;

        if (predicateEntry.isPresent())
            predicate = predicateEntry.get().getKey();

        if (predicate.test(tankCoord, turnCoordMap.get(enemyTank)))
            enemyTank.ride(enemyTank.getDir());
        else
            turnCoordMap.put(enemyTank, getTurnCoord());
    }


    private Coords getTurnCoord() {

        if (coordsToTargetMap.get(enemyTank).isEmpty()) {
            generatePath(this.enemyTank);
            getTurnCoord();
        }

        Coords first = coordsToTargetMap.get(enemyTank).peek();
        coordsToTargetMap.get(enemyTank).pop();

        if (coordsToTargetMap.get(enemyTank).isEmpty()) {
            generatePath(this.enemyTank);
            getTurnCoord();
        }

        Coords second = coordsToTargetMap.get(enemyTank).peek();

        Map<BiPredicate<Coords, Coords>, Direction> predicateMap = getPredicateMap();

        for (BiPredicate<Coords, Coords> predicate : predicateMap.keySet()) {
            Direction dir = predicateMap.get(predicate);
            if (predicate.test(first, second) && enemyTank.getBlockedDirection() != dir) {
                enemyTank.setDir(dir);
                break;
            }
        }

        return first;
    }

    private void generatePath(EnemyTank enemyTank) {
        Rectangle playerTankRect = playerTank.getSprite().getBoundingRectangle();

        UCSAlgo algo = new UCSAlgo(enemyTank.getSprite().getBoundingRectangle(), playerTankRect);
        List<Coords> algoCoords = algo.startAlgo();
        coordsToTargetMap.get(enemyTank).addAll(algoCoords);
    }

    private Map<BiPredicate<Coords, Coords>, Direction> getPredicateMap() {
        Map<BiPredicate<Coords, Coords>, Direction> predicateMap = new HashMap<>();

        predicateMap.put((coord1, coord2) -> coord1.x() < coord2.x(), Direction.RIGHT);
        predicateMap.put((coord1, coord2) -> coord1.x() > coord2.x(), Direction.LEFT);
        predicateMap.put((coord1, coord2) -> coord1.y() < coord2.y(), Direction.UP);
        predicateMap.put((coord1, coord2) -> coord1.y() > coord2.y(), Direction.DOWN);

        return predicateMap;
    }

    private boolean areTanksOnParallel() {
        Sprite tankSprite = enemyTank.getSprite();
        Coords coords = new Coords((int) tankSprite.getX(), (int) tankSprite.getY());
        Iterator<Entry<GameEntity, RectangleFloat>> iterator = rTreeMap.getParallelObstacles(enemyTank.getDir(), coords);

        while (iterator.hasNext()) {
            Entry<GameEntity, RectangleFloat> entry = iterator.next();
            if (entry.value() instanceof PlayerTank)
                return true;
        }

        return false;
    }*/

}

