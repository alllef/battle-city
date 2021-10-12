package com.github.alllef.battle_city.core.game_entity.coin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.github.alllef.battle_city.core.util.Drawable;

public enum CoinManager implements Drawable {
    INSTANCE;
    private final Array<Coin> coinArr = new Array<>();

    public Coin createCoin(float x, float y) {
        Coin coin = new Coin(x, y);
        coinArr.add(coin);
        return coin;
    }

    private void generateCoins(int coinNumber) {
        Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");
        int worldSize = prefs.getInteger("world_size");
        for (int i = 0; i < coinNumber; i++) {
            int x = (int) (Math.random() * worldSize * 0.95);
            int y = (int) (Math.random() * worldSize * 0.95);
            coinArr.add(new Coin(x, y));
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        coinArr.forEach(coin -> coin.getSprite().draw(spriteBatch));
    }
}
