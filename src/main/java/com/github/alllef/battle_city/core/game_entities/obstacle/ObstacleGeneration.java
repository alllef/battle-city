package com.github.alllef.battle_city.core.game_entities.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.SpriteParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ObstacleGeneration implements Drawable {
    private final Array<Obstacle> obstacles = new Array<>();


    public void generateObstacles(int obstacleSetsNumber) {
        Map<Rectangle, Obstacle> obstacleMap = new HashMap<>();

        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int sizeOfSet = new Random().nextInt(prefs.getInteger("obstacle_size_dispersion")) + prefs.getInteger("min_obstacle_set_size");

        for (int i = 0; i < obstacleSetsNumber; i++)
            generateObstacleSet(obstacleMap, sizeOfSet);

        obstacleMap.values().forEach(obstacles::add);
    }

    private void generateObstacleSet(Map<Rectangle, Obstacle> obstacleMap, int setSize) {
        Array<Obstacle> resultSet = new Array<>();
        boolean isNotGenerated = true;
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

        while (isNotGenerated) {
            Direction dir = Direction.values()[new Random().nextInt(Direction.values().length)];
            int x = (int) (Math.random() * prefs.getInteger("world_size"));
            int y = (int) (Math.random() * prefs.getInteger("worlds_size"));
            SpriteParam spriteParam = SpriteParam.OBSTACLE;

            for (int i = 0; i < setSize; i++) {
                int tmpX = x;
                int tmpY = y;

                switch (dir) {
                    case UP -> tmpY += (i * spriteParam.getHeight() + spriteParam.getHeight());
                    case DOWN -> tmpY -= (i * spriteParam.getHeight() + spriteParam.getHeight());
                    case RIGHT -> tmpX += (i * spriteParam.getWidth() + spriteParam.getWidth());
                    case LEFT -> tmpX -= (i * spriteParam.getWidth() + spriteParam.getWidth());
                }

                Obstacle obstacle = new Obstacle(tmpX, tmpY);
                if (obstacleMap.containsKey(obstacle.getSprite().getBoundingRectangle()))
                    break;

                resultSet.add(obstacle);
            }
            if (resultSet.size == setSize) isNotGenerated = false;
        }

        resultSet.forEach(result ->
                obstacleMap.put(result.getSprite().getBoundingRectangle(), result));
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        obstacles.forEach(value ->
                value.getSprite().draw(spriteBatch));
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }
}
