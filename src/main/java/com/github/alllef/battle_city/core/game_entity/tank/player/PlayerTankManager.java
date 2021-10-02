package com.github.alllef.battle_city.core.game_entity.tank.player;

import com.github.alllef.battle_city.core.world.RTreeMap;

public enum PlayerTankManager {
    INSTANCE;
    protected RTreeMap rTreeMap = RTreeMap.getInstance();
    private  final PlayerTank playerTank = PlayerTank.getInstance();
    private  final PlayerTank aiPlayerTank = AIPlayerTankWrapper.getInstance();


    enum TankType {AI_PLAYER_TANK, USER_PLAYER_TANK}

    TankType type;

    public  PlayerTank getInstance() {
        return aiPlayerTank;
    }


    public void ride() {
        aiPlayerTank.ride();
    }



}
