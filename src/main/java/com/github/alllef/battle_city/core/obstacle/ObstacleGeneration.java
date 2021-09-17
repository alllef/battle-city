package com.github.alllef.battle_city.core.obstacle;

import com.badlogic.gdx.utils.Array;

public class ObstacleGeneration {
    Array<Obstacle> obstacleArray = new Array<>();

    public void generateObstacles(int obstacleNumber) {
        for (int i = 0; i < obstacleNumber; i++) {
            int x = (int) (Math.random() * 100);
            int y = (int) (Math.random() * 100);
            obstacleArray.add(new Obstacle(x, y));
        }
    }

    public Array<Obstacle> getObstacleArray() {
        return obstacleArray;
    }
}
