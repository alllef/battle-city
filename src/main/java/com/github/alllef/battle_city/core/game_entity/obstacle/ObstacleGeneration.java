package com.github.alllef.battle_city.core.game_entity.obstacle;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;

import java.util.*;

public class ObstacleGeneration extends EntityManager<Obstacle> {

    public ObstacleGeneration(Preferences prefs) {
        super(prefs);
        generateObstacles();
    }

    private void generateObstacles() {
        if (prefs.getBoolean("is_random"))
            generateRandomObstacles(prefs.getInteger("obstacle_sets"));
        else
            generateObstaclesByLevel();
    }

    private void generateObstaclesByLevel() {
        List<Coords> obstacleCoords = new LevelParser(prefs.getString("level")).parseFile();
        for (Coords coord : obstacleCoords) {
            Obstacle tmp = new Obstacle(coord.x(), coord.y());
            entityArr.add(tmp);
        }
    }

    private void generateRandomObstacles(int obstacleSetsNum) {
        Map<Rectangle, Obstacle> obstacleMap = new HashMap<>();

        for (int i = 0; i < obstacleSetsNum; i++)
            generateObstacleSet(obstacleMap).forEach(result ->
                    obstacleMap.put(result.getRect(), result));

        obstacleMap.values().forEach(entityArr::add);
    }

    private Array<Obstacle> generateObstacleSet(Map<Rectangle, Obstacle> obstacleMap) {
        Array<Obstacle> resultSet;
        Random rand = new Random();
        int setSize;

        do {
            resultSet = new Array<>();
            Direction dir = Direction.values()[rand.nextInt(Direction.values().length)];
            Coords coords = getObstacleSetStartCoords(obstacleMap.values());
            setSize = rand.nextInt(prefs.getInteger("obstacle_size_dispersion"))
                    + prefs.getInteger("min_obstacle_set_size");

            for (int i = 0; i < setSize; i++) {
                Obstacle obstacle = getObstacleByCoords(i, coords, dir);

                if (obstacleMap.containsKey(obstacle.getRect()))
                    break;

                resultSet.add(obstacle);
            }
        }
        while (resultSet.size < setSize);

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
        int height = (int) spriteParam.getHeight();
        int width = (int) spriteParam.getWidth();

        switch (dir) {
            case UP -> tmpY += (number * height + height);
            case DOWN -> tmpY -= (number * height + height);
            case RIGHT -> tmpX += (number * width + width);
            case LEFT -> tmpX -= (number * width + width);
        }

        return new Obstacle(tmpX, tmpY);
    }

    public void bulletShoot(Obstacle obstacle) {
        this
                .getEntities()
                .removeValue(obstacle, true);
    }
}
