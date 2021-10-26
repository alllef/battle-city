package com.github.alllef.battle_city.core.game_entity.tank.enemy.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.util.enums.SpriteParam;
import com.github.alllef.battle_city.core.world.RTreeMap;

public class RandomReflexEnemyTank extends ReflexEnemyTank {

    public RandomReflexEnemyTank(RTreeMap rTreeMap, BulletFactory bulletFactory, float x, float y, Preferences prefs) {
        super(rTreeMap, bulletFactory, x, y, prefs);
        sprite.setTexture(new Texture(Gdx.files.internal(SpriteParam.ENEMY_RANDOM_TANK.getTexturePath())));

    }
}
