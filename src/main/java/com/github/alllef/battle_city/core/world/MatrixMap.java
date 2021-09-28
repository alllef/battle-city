package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.GameEntity;

public class MatrixMap extends WorldMap {
    private final boolean[][] entityMatrix;

    public MatrixMap() {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        entityMatrix = new boolean[worldSize][worldSize];
        createEntityMatrix();
    }

    private void createEntityMatrix() {
        Array<GameEntity> entities = getEntitiesArray();
        entities.addAll(obstacleGeneration.getObstacles());
        entities.forEach(this::setEntityOnMatrix);
    }

    private void setEntityOnMatrix(GameEntity entity) {
        Sprite sprite = entity.getSprite();

        int x = (int) sprite.getX();
        int y = (int) sprite.getY();
        int boundX = x + (int) sprite.getWidth();
        int boundY = y + (int) sprite.getHeight();

        for (; x <= boundX; x++) {
            for (; y <= boundY; y++) {
                try {
                        entityMatrix[x][y] = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(x + " " + y);
                    System.out.println(sprite.getWidth() + " " + sprite.getHeight());
                    System.out.println(entity.toString());
                }

            }
        }
    }
   /* public Array<SingleTank> getAllTanks() {
        Array<SingleTank> allTanks = new Array<>();
        enemyTankManager.getEnemyTanks().forEach(allTanks::add);
        allTanks.add(playerTank);
        return allTanks;
    }*/

}

