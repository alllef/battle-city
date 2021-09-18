package com.github.alllef.battle_city.core.obstacle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ObstacleGeneration implements Drawable {
    private Array<Obstacle> obstacles = new Array<>();


    public void generateObstacles(int obstacleSetsNumber) {
        Map<Rectangle, Obstacle> obstacleMap = new HashMap<>();
        int sizeOfSet = new Random().nextInt(15) + 10;
        for (int i = 0; i < obstacleSetsNumber; i++)
            generateObstacleSet(obstacleMap,sizeOfSet);

        obstacleMap.values().forEach(value->obstacles.add(value));
    }

    private void generateObstacleSet(Map<Rectangle, Obstacle> obstacleMap, int setSize) {
        Array<Obstacle> resultSet = new Array<>();
        boolean isNotGenerated = true;

        while (isNotGenerated) {
            System.out.println("wtf");
            Direction dir = Direction.values()[new Random().nextInt(Direction.values().length)];
            int x = (int) (Math.random() * 100);
            int y = (int) (Math.random() * 100);

            for (int i = 0; i < setSize; i++) {
                int tmpX = x;
                int tmpY = y;

                switch (dir) {
                    case UP -> tmpY += i;
                    case DOWN -> tmpY -= (i + 1);
                    case RIGHT -> tmpX += i + 2;
                    case LEFT -> tmpX -= (i + 2);
                }

                Obstacle obstacle = new Obstacle(tmpX, tmpY);
                if (obstacleMap.containsKey(obstacle.getObstacleSprite().getBoundingRectangle()))
                    break;

                resultSet.add(obstacle);
            }
            if (resultSet.size == setSize) isNotGenerated = false;
        }

        resultSet.forEach(result ->
                obstacleMap.put(result.getObstacleSprite().getBoundingRectangle(), result));
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        obstacles
                .forEach(value -> {
                    value.getObstacleSprite().draw(spriteBatch);
                });
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }
}
