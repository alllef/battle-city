package com.github.alllef.battle_city.core.game_entity.coin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.world.RTreeMap;

public class CoinManager extends EntityManager<Coin> {

    public CoinManager(int coinNum) {
        generateCoins(coinNum);
    }

    private void generateCoins(int coinNumber) {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        for (int i = 0; i < coinNumber; i++) {
            int x = (int) (Math.random() * worldSize * 0.95);
            int y = (int) (Math.random() * worldSize * 0.95);
            entityArr.add(new Coin(x, y));
        }
    }

    public Array<Coin> getCoinArr() {
        return entityArr;
    }
}
