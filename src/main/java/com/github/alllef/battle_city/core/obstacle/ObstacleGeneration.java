package com.github.alllef.battle_city.core.obstacle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ObstacleGeneration implements Drawable {

    private Map<Rectangle, Obstacle> obstacleArray = new HashMap<>();

    public void generateObstacles(int obstacleSetsNumber) {
        int sizeOfSet = new Random().nextInt(15) + 10;
        for (int i = 0; i < obstacleSetsNumber; i++)
            generateObstacleSet(sizeOfSet);
    }

    private void generateObstacleSet(int setSize) {
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
                if (obstacleArray.containsKey(obstacle.getObstacleSprite().getBoundingRectangle()))
                    break;

                resultSet.add(obstacle);
            }
            if (resultSet.size == setSize) isNotGenerated = false;
        }

        resultSet.forEach(result ->
                obstacleArray.put(result.getObstacleSprite().getBoundingRectangle(), result));
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        obstacleArray.values()
                .forEach(value -> {
                    value.getObstacleSprite().draw(spriteBatch);
                    System.out.println(value.obstacleSprite.getX() + " " + value.getObstacleSprite().getY());
                });
    }
}
