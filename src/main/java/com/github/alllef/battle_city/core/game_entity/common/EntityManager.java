package com.github.alllef.battle_city.core.game_entity.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.interfaces.Drawable;
import com.github.alllef.battle_city.core.util.interfaces.Updatable;

public abstract class EntityManager<T extends GameEntity> implements Drawable, Updatable {
    protected final Array<T> entityArr = new Array<>();
    protected  final Preferences prefs;

    public EntityManager(Preferences prefs) {
        this.prefs = prefs;
    }

    public Array<T> getEntities() {
        return entityArr;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        entityArr.forEach(entity -> entity.draw(spriteBatch));
    }

    @Override
    public void update() {}
}
