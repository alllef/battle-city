package com.github.alllef.battle_city.core.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.util.Direction;

import java.awt.*;
import java.util.Random;

public class EnemyTank extends SingleTank {

    public EnemyTank() {
        super("sprites/enemy.png");
    }

    public EnemyTank(float x, float y){
        super("sprites/enemy.png");
        this.getTankSprite().setPosition(x,y);
    }

    @Override
    public void shoot() {
        setDurationBetweenBullets(3*500);
        super.shoot();
    }
}
