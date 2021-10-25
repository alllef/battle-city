package com.github.alllef.battle_city.core.game_entity.tank.enemy.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;

import com.github.alllef.battle_city.core.util.enums.SpriteParam;
import com.github.alllef.battle_city.core.world.RTreeMap;

public class PlayerReflexEnemyTank extends ReflexEnemyTank {

    public PlayerReflexEnemyTank(RTreeMap rTreeMap, BulletFactory bulletFactory, float x, float y) {
        super(rTreeMap, bulletFactory, x, y);
        sprite.setTexture(new Texture(Gdx.files.internal(SpriteParam.ENEMY_PLAYER_TANK.getTexturePath())));
    }
}
