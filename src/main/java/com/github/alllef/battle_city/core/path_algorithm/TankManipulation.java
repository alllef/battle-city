package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos.BFSAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos.UCSAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.other_algos.DFSAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab2.AStarAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.RTreeMap;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum TankManipulation implements Drawable {
    INSTANCE;

    AlgoType algoType = AlgoType.BFS;
    PlayerTank playerTank = PlayerTank.getInstance();
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    PathAlgo pathAlgo;
    RTreeMap rTreeMap = RTreeMap.getInstance();
    List<List<Coords>> pathsToDraw = new ArrayList<>();
    GdxToRTreeRectangleMapper rectMapper = GdxToRTreeRectangleMapper.ENTITY;
    int attempts = 0;

    public void setPathAlgo(PathAlgo pathAlgo) {
        this.pathAlgo = pathAlgo;
    }

    public void update() {
        attempts++;
        if (attempts > 50) {
            attempts = 0;
            pathsToDraw.clear();
            long seconds = TimeUtils.millis();
            enemyTankManager.getEnemyTanks().forEach(enemyTank -> {

                        PathAlgo<Collection<Coords>> algo = /*new AStarAlgo(playerTank,enemyTank);*/getPathAlgo(enemyTank);
                        List<Coords> coords = algo.createAlgo();
                        pathsToDraw.add(coords);
                    }
            );
            System.out.println(TimeUtils.millis() - seconds);
        }
    }

    private PathAlgo<Collection<Coords>> getPathAlgo(GameEntity endEntity) {
        PathAlgo pathAlgo = null;
        Rectangle playerRect = playerTank.getSprite().getBoundingRectangle();
        Rectangle endRect = endEntity.getSprite().getBoundingRectangle();

        switch (algoType) {
            case BFS -> pathAlgo = new BFSAlgo(playerRect, endRect);
            case DFS -> pathAlgo = new DFSAlgo(playerRect, endRect);
            case UCS -> pathAlgo = new UCSAlgo(playerRect, endRect);
        }

        return pathAlgo;
    }


    private List<Coords> getAStarSeveralDots(Rectangle start, Rectangle end, int dotsNum) {
        List<Coords> path = new ArrayList<>();
        List<Rectangle> dots = new ArrayList<>();

        dots.add(start);
        for (int i = 0; i < dotsNum; i++) {
            Coords coords = rTreeMap.getRandomNonObstacleCoord();
            Rectangle gdxRect = rectMapper.convertToGdxRectangle(rTreeMap.getSmallestRect(coords));
            dots.add(gdxRect);
        }
        dots.add(end);

        for (int i = 0; i < dots.size() - 1; i++) {
            List<Coords> partialPath = new AStarAlgo(dots.get(i), dots.get(i + 1)).createAlgo();
            if (partialPath.isEmpty()) return partialPath;
            path.addAll(partialPath);
        }

        return path;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List<Color> possibleColors = List.of(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE);
        Texture obstacle = new Texture(SpriteParam.OBSTACLE.getTexturePath());
        ShapeDrawer shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(obstacle));

        for (int i = 0; i < pathsToDraw.size(); i++) {
            Color tmp = possibleColors.get(i);
            tmp.a = 0.5f;
            shapeDrawer.setColor(tmp);

            pathsToDraw.get(i).forEach(coords ->
                    shapeDrawer.line(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1));
        }
    }

    public AlgoType getAlgoType() {
        return algoType;
    }

    public void setAlgoType(AlgoType algoType) {
        this.algoType = algoType;
    }
}
