package com.github.alllef.battle_city.core.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Obstacle {
    Sprite obstacleSprite = new Sprite(new Texture(Gdx.files.internal("sprites/block.png")));

    public Obstacle(int x, int y) {
        obstacleSprite.setSize(2,1);
        obstacleSprite.setPosition(x,y);
    }

    public Sprite getObstacleSprite() {
        return obstacleSprite;
    }

    public void setObstacleSprite(Sprite obstacleSprite) {
        this.obstacleSprite = obstacleSprite;
    }
}
