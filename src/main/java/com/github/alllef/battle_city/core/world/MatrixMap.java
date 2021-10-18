package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;

public class MatrixMap extends WorldMap {
    private static final MatrixMap matrixMap = new MatrixMap();

    public static MatrixMap getInstance() {
        return matrixMap;
    }

    private boolean[][] entityMatrix;

    private MatrixMap() {
        createEntityMatrix();
    }

    private void createEntityMatrix() {
        int worldSize = prefs.getInteger("world_size");
        entityMatrix = new boolean[worldSize][worldSize];
        getEntitiesArray().forEach(this::setEntityOnMatrix);
    }

    private void setEntityOnMatrix(GameEntity entity) {
        Sprite sprite = entity.getSprite();

        int x = (int) sprite.getX();
        int y = (int) sprite.getY();
        int boundX = x + (int) sprite.getWidth();
        int boundY = y + (int) sprite.getHeight();

        for (; x <= boundX; x++) {
            for (; y <= boundY; y++) {
                    if (x < entityMatrix.length && y < entityMatrix.length && x >= 0 && y >= 0)
                        entityMatrix[x][y] = true;
            }
        }
    }

    @Override
    public void update() {
        createEntityMatrix();
    }

    public boolean[][] getEntityMatrix() {
        return entityMatrix;
    }
}

