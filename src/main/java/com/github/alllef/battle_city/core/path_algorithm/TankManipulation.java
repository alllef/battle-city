package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.path_algorithm.lab1.BFSAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Drawable;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.List;

public class TankManipulation implements Drawable {
    PlayerTank playerTank = PlayerTank.getInstance();
    EnemyTankManager enemyTankManager = EnemyTankManager.getInstance();
    PathAlgo pathAlgo;

    List<Coords> pathsToDraw = new ArrayList<>();
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

                        BFSAlgo bfsAlgo = new BFSAlgo(playerTank.getSprite().getBoundingRectangle(), enemyTank.getSprite().getBoundingRectangle());
                        List<Coords> coords = bfsAlgo.createAlgo();
                        pathsToDraw.addAll(coords);
                    }
            );
            System.out.println(TimeUtils.millis() - seconds);
        }
    }


    @Override
    public void draw(SpriteBatch spriteBatch) {
        ShapeDrawer shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(new Texture(Gdx.files.internal("sprites/block.png"))));
        shapeDrawer.setColor(Color.YELLOW);
        pathsToDraw.forEach(coords ->
                shapeDrawer.filledRectangle(coords.x(), coords.y(), 1, 1));

    }
}
