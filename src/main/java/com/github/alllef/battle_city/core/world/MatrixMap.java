package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;

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

    @Override
    public void update() {
        createEntityMatrix();
    }
   /* public Array<SingleTank> getAllTanks() {
        Array<SingleTank> allTanks = new Array<>();
        enemyTankManager.getEnemyTanks().forEach(allTanks::add);
        allTanks.add(playerTank);
        return allTanks;
    }*/

}
