package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.world.MatrixMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class PathAlgo {
    protected boolean[][] entityMatr = MatrixMap.getInstance().getEntityMatrix();
    protected static Coords[][] coordsMatr;

    static {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        coordsMatr = new Coords[prefs.getInteger("world_size")][prefs.getInteger("world_size")];
        for (int i = 0; i < coordsMatr.length; i++) {
            for (int j = 0; j < coordsMatr.length; j++) {
                coordsMatr[i][j] = new Coords(i, j);
            }
        }
    }

    private Rectangle startRect;
    private Rectangle endRect;

    public PathAlgo(Rectangle startRect, Rectangle endRect) {
        this.startRect = startRect;
        this.endRect = endRect;
    }


    protected List<Coords> getAdjacentVertices(Coords coords) {
        List<Coords> adjacent = new LinkedList<>();

        if (coords.x() > 0 && !entityMatr[coords.x() - 1][coords.y()])
            adjacent.add(coordsMatr[coords.x() - 1][coords.y()]);

        if (coords.y() > 0 && !entityMatr[coords.x()][coords.y() - 1])
            adjacent.add(coordsMatr[coords.x()][coords.y() - 1]);

        if (coords.x() < entityMatr.length - 1 && !entityMatr[coords.x() + 1][coords.y()])
            adjacent.add(coordsMatr[coords.x() + 1][coords.y()]);

        if (coords.y() < entityMatr.length - 1 && !entityMatr[coords.x()][coords.y() + 1])
            adjacent.add(coordsMatr[coords.x()][coords.y() + 1]);

        return adjacent;
    }

    public abstract List<Coords> createAlgo();

   protected Coords getFirstVertex(){
        return getVertexNearest(startRect);
    }

    private Coords getVertexNearest(Rectangle rectangle) {

        int width = (int) rectangle.getWidth();
        int height = (int) rectangle.getHeight();
        int x = (int) rectangle.getX();
        int y = (int) rectangle.getY();

        int yUp = y + height + 1;
        int yDown = y - 1;
        int xRight = x + width + 1;
        int xLeft = x - 1;


        for (int tmpX = x; tmpX <= tmpX + width; tmpX++) {
            int tmpY = yUp;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                return new Coords(tmpX, tmpY);
        }

        for (int tmpX = x; tmpX <= tmpX + width; tmpX++) {
            int tmpY = yDown;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                return new Coords(tmpX, tmpY);
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xRight;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                return new Coords(tmpX, tmpY);
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xLeft;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                return new Coords(tmpX, tmpY);
        }

        return null;
    }

    protected boolean isMatrixPart(Coords coords) {

        int width = (int) endRect.getWidth();
        int height = (int) endRect.getHeight();
        int x = (int) endRect.getX();
        int y = (int) endRect.getY();

        int yUp = y + height + 1;
        int yDown = y - 1;
        int xRight = x + width + 1;
        int xLeft = x - 1;


        for (int tmpX = x; tmpX <= tmpX + width; tmpX++) {
            int tmpY = yUp;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                if (coords.equals(new Coords(tmpX,tmpY))) return true;
        }

        for (int tmpX = x; tmpX <= tmpX + width; tmpX++) {
            int tmpY = yDown;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                if (coords.equals(new Coords(tmpX,tmpY))) return true;
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xRight;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                if (coords.equals(new Coords(tmpX,tmpY))) return true;
        }

        for (int tmpY = y; tmpY <= tmpY + height; tmpY++) {
            int tmpX = xLeft;
            if (tmpY < entityMatr.length && tmpX < entityMatr.length && tmpY >= 0 && tmpX >= 0 && !entityMatr[tmpX][tmpY])
                if (coords.equals(new Coords(tmpX,tmpY))) return true;
        }

        return false;
    }
}
