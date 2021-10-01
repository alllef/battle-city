package com.github.alllef.battle_city.core.game_entity.tank.player;

import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;

public class PlayerTankManager {
    private static final PlayerTank playerTank = new PlayerTank(BulletFactory.INSTANCE);
    private static final PlayerTank aiPlayerTank = new AIPlayerTank(BulletFactory.INSTANCE);

    public static PlayerTank getPlayerTank() {
        return playerTank;
    }

    public static  PlayerTank getAiPlayerTank(){return aiPlayerTank;}
}
