package com.github.alllef.battle_city.core.game_entity.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

public abstract class EntityManager<T extends GameEntity>  {
    protected final Array<T> entityArr = new Array<>();
    protected Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    public Array<T> getEntities() {
        return entityArr;
    }

}
