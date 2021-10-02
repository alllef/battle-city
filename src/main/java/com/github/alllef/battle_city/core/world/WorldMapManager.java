package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.tank.player.AIPlayerTankWrapper;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.util.Coords;

public class WorldMapManager extends WorldMap {

    private static final WorldMapManager worldMapManager = new WorldMapManager();

    public static WorldMapManager getInstance() {
        return worldMapManager;
    }

    private final RTreeMap rTreeMap = RTreeMap.getInstance();
    PlayerTank aIPlayerTankWrapper = AIPlayerTankWrapper.getInstance();

    @Override
    public void update() {
        rTreeMap.update();
        bulletFactory.updateBullets();
        enemyTankManager.ride();
        enemyTankManager.shoot();
        aIPlayerTankWrapper.ride();
        aIPlayerTankWrapper.shoot();
    }

}
