package com.github.alllef.battle_city.core.obstacle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.Drawable;

public class ObstacleGeneration implements Drawable {
   private Array<Obstacle> obstacleArray = new Array<>();

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

    @Override
    public void draw(SpriteBatch spriteBatch) {
        this.getObstacleArray()
                .forEach(obstacle -> obstacle.getObstacleSprite().draw(spriteBatch));

    }
}
