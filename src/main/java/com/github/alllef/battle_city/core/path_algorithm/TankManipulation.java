package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.AlgoType;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.DFSAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.PathAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.bfs_like_algos.BFSAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.bfs_like_algos.UCSAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Drawable;
import space.earlygrey.shapedrawer.ShapeDrawer;

import javax.swing.plaf.synth.ColorType;
import java.util.ArrayList;
import java.util.List;

public class TankManipulation implements Drawable {
    AlgoType algoType = AlgoType.BFS;
    PlayerTank playerTank = PlayerTank.getInstance();
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    PathAlgo pathAlgo;

    List<List<Coords>> pathsToDraw = new ArrayList<>();
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

                        PathAlgo algo = getPathAlgo(enemyTank);
                        List<Coords> coords = algo.createAlgo();
                        pathsToDraw.add(coords);
                    }
            );
            System.out.println(TimeUtils.millis() - seconds);
        }
    }

    private PathAlgo getPathAlgo(GameEntity endEntity) {
        PathAlgo pathAlgo = null;

        switch (algoType) {
            case BFS -> pathAlgo = new BFSAlgo(playerTank, endEntity);
            case DFS -> pathAlgo = new DFSAlgo(playerTank, endEntity);
            case UCS -> pathAlgo = new UCSAlgo(playerTank, endEntity);
        }

        return pathAlgo;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        List<Color> possibleColors = List.of(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE);
        ShapeDrawer shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(new Texture(Gdx.files.internal("sprites/block.png"))));

        for (int i = 0; i < pathsToDraw.size(); i++) {
            Color tmp = possibleColors.get(i);
            tmp.a = 0.5f;
            shapeDrawer.setColor(tmp);

            pathsToDraw.get(i).forEach(coords ->
                    shapeDrawer.line(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1));
        }
    }

    public void setAlgoType(AlgoType algoType) {
        this.algoType = algoType;
    }
}
