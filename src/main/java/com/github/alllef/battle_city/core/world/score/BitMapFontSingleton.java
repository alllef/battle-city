package com.github.alllef.battle_city.core.world.score;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BitMapFontSingleton {
    private static final BitmapFont font = new BitmapFont();

    public static BitmapFont getInstance() {
        return font;
    }
}
