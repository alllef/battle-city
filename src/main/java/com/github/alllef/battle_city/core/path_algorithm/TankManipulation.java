package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos.BFSAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.bfs_like_algos.UCSAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab1.other_algos.DFSAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab2.AStarAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.ExpectiMaxAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta.MiniMaxAlphaBetaAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.SpriteParam;
import com.github.alllef.battle_city.core.util.mapper.GdxToRTreeRectangleMapper;
import com.github.alllef.battle_city.core.world.RTreeMap;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.List;

public enum TankManipulation implements Drawable {
    INSTANCE;

    PlayerTank playerTank = PlayerTank.getInstance();
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    RTreeMap rTreeMap = RTreeMap.getInstance();
    GdxToRTreeRectangleMapper rectMapper = GdxToRTreeRectangleMapper.ENTITY;
    Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
    int manipulation = 0;
    AlgoType algoType = AlgoType.BFS;
    PathAlgo pathAlgo;
    int frames = 0;
    List<List<Coords>> pathsToDraw = new ArrayList<>();

    public void setPathAlgo(PathAlgo pathAlgo) {
        this.pathAlgo = pathAlgo;
    }

    public void update() {
        if (frames < 50)
            frames++;
        else
            updatePath();
    }

    public void updatePath() {
        frames = 0;
        pathsToDraw.clear();
        long seconds = TimeUtils.millis();

        // enemyTankManager.getEntities().forEach(enemyTank -> pathsToDraw.add(getPathAlgo(enemyTank)));
        ExpectiMaxAlgo algo = new ExpectiMaxAlgo(enemyTankManager.getEntities().get(0).getSprite().getBoundingRectangle(), playerTank.getSprite().getBoundingRectangle(), enemyTankManager.getEntities().get(0).getDir());
        algo.startAlgo(1);
        manipulation++;
        System.out.println("manipulation" + manipulation);
        if (manipulation > 5) {
            System.out.println();
            System.out.println("manipulation>5");
        }
        System.out.println(TimeUtils.millis() - seconds);
    }

    private List<Coords> getPathAlgo(GameEntity endEntity) {
        int labNum = prefs.getInteger("lab_number");
        if (labNum == 1)
            return getPathAlgoLab1(endEntity);
        if (labNum == 2)
            return getPathAlgoLab2(endEntity);

        return new ArrayList<>();
    }

    private List<Coords> getPathAlgoLab1(GameEntity endEntity) {
        PathAlgo pathAlgo = null;
        Rectangle playerRect = playerTank.getSprite().getBoundingRectangle();
        Rectangle endRect = endEntity.getSprite().getBoundingRectangle();

        switch (algoType) {
            case BFS -> pathAlgo = new BFSAlgo(playerRect, endRect);
            case DFS -> pathAlgo = new DFSAlgo(playerRect, endRect);
            case UCS -> pathAlgo = new UCSAlgo(playerRect, endRect);
        }

        return pathAlgo.startAlgo();
    }


    private List<Coords> getPathAlgoLab2(GameEntity endEntity) {
        Rectangle playerRect = playerTank.getSprite().getBoundingRectangle();
        Rectangle endRect = endEntity.getSprite().getBoundingRectangle();
        if (algoType == AlgoType.ASTAR_N)
            return getAStarSeveralDots(playerRect, endRect, prefs.getInteger("dots_number_astar"));

        return new AStarAlgo(playerRect, endRect, algoType).startAlgo();
    }

    private List<Coords> getAStarSeveralDots(Rectangle start, Rectangle end, int dotsNum) {
        List<Coords> path = new ArrayList<>();
        List<Rectangle> dots = getPathDots(start, end, dotsNum);

        for (int i = 0; i < dots.size() - 1; i++) {
            List<Coords> partialPath = new AStarAlgo(dots.get(i), dots.get(i + 1), AlgoType.ASTAR_COORDS).startAlgo();
            if (partialPath.isEmpty()) return partialPath;
            path.addAll(partialPath);
        }

        return path;
    }

    private List<Rectangle> getPathDots(Rectangle start, Rectangle end, int dotsNum) {
        List<Rectangle> dots = new ArrayList<>();

        dots.add(start);
        for (int i = 0; i < dotsNum; i++) {
            Coords coords = rTreeMap.getRandomNonObstacleCoord();
            Rectangle gdxRect = rectMapper.convertToGdxRectangle(rTreeMap.getSmallestRect(coords));
            dots.add(gdxRect);
        }
        dots.add(end);

        return dots;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        Texture obstacle = new Texture(SpriteParam.OBSTACLE.getTexturePath());
        ShapeDrawer shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(obstacle));

        for (int i = 0; i < pathsToDraw.size(); i++) {
            shapeDrawer.setColor(chooseColor(i));

            pathsToDraw.get(i).forEach(coords ->
                    shapeDrawer.line(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1));
        }
    }

    public Color chooseColor(int colorNum) {
        List<Color> possibleColors = List.of(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE);
        Color tmp = possibleColors.get(colorNum);
        tmp.a = 0.5f;
        return tmp;
    }

    public AlgoType getAlgoType() {
        return algoType;
    }

    public void setAlgoType(AlgoType algoType) {
        this.algoType = algoType;
    }
}
