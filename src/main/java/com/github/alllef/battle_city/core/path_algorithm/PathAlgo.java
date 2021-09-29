package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.game_entity.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.PlayerTank;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.world.MatrixMap;

import java.util.ArrayList;
import java.util.List;

public abstract class PathAlgo {
    protected boolean[][] entityMatr = MatrixMap.getInstance().getEntityMatrix();
    private Rectangle startRect;
    private Rectangle endRect;

    public PathAlgo(Rectangle startRect, Rectangle endRect) {
        this.startRect = startRect;
        this.endRect = endRect;
    }

    protected List<Coords> getAdjacentVertices(Coords coords) {
        List<Coords> adjacent = new ArrayList<>();

        if (coords.x() > 0 && !entityMatr[coords.x()][coords.y()])
            adjacent.add(new Coords(coords.x() - 1, coords.y()));
        if (coords.y() > 0 && !entityMatr[coords.x()][coords.y()])
            adjacent.add(new Coords(coords.x(), coords.y() - 1));
        if (coords.x() < entityMatr.length - 1 && !entityMatr[coords.x()][coords.y()])
            adjacent.add(new Coords(coords.x() + 1, coords.y()));
        if (coords.y() < entityMatr.length - 1 && !entityMatr[coords.x()][coords.y()])
            adjacent.add(new Coords(coords.x(), coords.y() + 1));

        return adjacent;
    }

    public abstract List<Coords> createAlgo();

    protected Coords getFirstVertix() {

        int width = (int) startRect.getWidth();
        int height = (int) startRect.getHeight();
        int x = (int) startRect.getX();
        int y = (int) startRect.getY();

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
        int x = (int) endRect.getX();
        int y = (int) endRect.getY();
        int boundX = x + (int) endRect.getWidth();
        int boundY = y + (int) endRect.getHeight();

        for (; x <= boundX; x++) {
            for (; y <= boundY; y++) {
                if (x < entityMatr.length && y < entityMatr.length && x >= 0 && y >= 0 && x == coords.x() && y == coords.y())
                    return true;
            }
        }

        return false;
    }
}
