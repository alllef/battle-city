package com.github.alllef.battle_city.core.world;

public class WorldMapManager extends WorldMap {

    private static final WorldMapManager worldMapManager = new WorldMapManager();
    public static WorldMapManager getInstance() {
        return worldMapManager;
    }

    private final MatrixMap matrixMap = MatrixMap.getInstance();
    private final RTreeMap rtreeMap = RTreeMap.getInstance();

    @Override
    public void update() {
        rtreeMap.update();
        matrixMap.update();

        bulletFactory.updateBullets();
        enemyTankManager.ride();
        enemyTankManager.shoot();
        playerTank.ride();
    }
}
