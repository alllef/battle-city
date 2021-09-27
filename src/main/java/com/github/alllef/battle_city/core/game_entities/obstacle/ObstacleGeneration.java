package com.github.alllef.battle_city.core.game_entities.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entities.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entities.tank.EnemyTankManager;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.util.Drawable;
import com.github.alllef.battle_city.core.util.SpriteParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ObstacleGeneration implements Drawable {
    private static ObstacleGeneration obstacleGeneration;

    public static ObstacleGeneration getInstance() {
        if (obstacleGeneration == null) {
            Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
            obstacleGeneration = new ObstacleGeneration(prefs.getInteger("obstacle_sets"));
        }
        return obstacleGeneration;
    }

    private final Array<Obstacle> obstacles = new Array<>();
    private final Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    private record Coords(int x, int y) {
    }

    public ObstacleGeneration(int obstacleSetsNumber) {
        generateObstacles(obstacleSetsNumber);
    }

    private void generateObstacles(int obstacleSetsNumber) {
        Map<Rectangle, Obstacle> obstacleMap = new HashMap<>();

        for (int i = 0; i < obstacleSetsNumber; i++)
            generateObstacleSet(obstacleMap).forEach(result ->
                    obstacleMap.put(result.getSprite().getBoundingRectangle(), result));

        obstacleMap.values().forEach(obstacles::add);
    }

    private Array<Obstacle> generateObstacleSet(Map<Rectangle, Obstacle> obstacleMap) {
        Array<Obstacle> resultSet = new Array<>();
        Random rand = new Random();
        int setSize;

        do {
            Direction dir = Direction.values()[rand.nextInt(Direction.values().length)];
            Coords coords = getObstacleSetStartCoords(obstacleMap.values());
            setSize = rand.nextInt(prefs.getInteger("obstacle_size_dispersion"))
                    + prefs.getInteger("min_obstacle_set_size");

            for (int i = 0; i < setSize; i++) {
                Obstacle obstacle = getObstacleByCoords(i, coords, dir);

                if (obstacleMap.containsKey(obstacle.getSprite().getBoundingRectangle()))
                    break;

                resultSet.add(obstacle);
            }
        }
        while (resultSet.size != setSize);

        return resultSet;
    }

    private Coords getObstacleSetStartCoords(Collection<Obstacle> obstacleCollection) {
        Random rand = new Random();
        int x;
        int y;
        int worldSize = prefs.getInteger("world_size");

        if (obstacleCollection.isEmpty()) {
            x = rand.nextInt(worldSize);
            y = rand.nextInt(worldSize);
        } else {
            Obstacle[] obstacles = new Obstacle[obstacleCollection.size()];
            obstacleCollection.toArray(obstacles);
            Obstacle chosen = obstacles[rand.nextInt(obstacles.length)];
            x = (int) chosen.getSprite().getX();
            y = (int) chosen.getSprite().getY();
        }
        return new Coords(x, y);
    }

    private Obstacle getObstacleByCoords(int number, Coords startCoords, Direction dir) {
        int tmpX = startCoords.x();
        int tmpY = startCoords.y();
        SpriteParam spriteParam = SpriteParam.OBSTACLE;

        switch (dir) {
            case UP -> tmpY += (number * spriteParam.getHeight() + spriteParam.getHeight());
            case DOWN -> tmpY -= (number * spriteParam.getHeight() + spriteParam.getHeight());
            case RIGHT -> tmpX += (number * spriteParam.getWidth() + spriteParam.getWidth());
            case LEFT -> tmpX -= (number * spriteParam.getWidth() + spriteParam.getWidth());
        }

        return new Obstacle(tmpX, tmpY);
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
