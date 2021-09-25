package com.github.alllef.battle_city.core.obstacle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.util.SpriteParam;

public class Obstacle {
    Sprite obstacleSprite = new Sprite(new Texture(SpriteParam.OBSTACLE.getTexturePath()));

    public Obstacle(int x, int y) {
        obstacleSprite.setSize(SpriteParam.OBSTACLE.getWidth(),SpriteParam.OBSTACLE.getHeight());
        obstacleSprite.setPosition(x,y);
    }

    public Sprite getObstacleSprite() {
        return obstacleSprite;
    }

    public void setObstacleSprite(Sprite obstacleSprite) {
        this.obstacleSprite = obstacleSprite;
    }
}
