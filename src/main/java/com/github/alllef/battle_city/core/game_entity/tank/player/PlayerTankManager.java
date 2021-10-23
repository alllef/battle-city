package com.github.alllef.battle_city.core.game_entity.tank.player;

import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.util.Direction;

public class PlayerTankManager extends EntityManager<PlayerTank> {
    private static PlayerTankManager playerTankManager;
    private BulletFactory bulletFactory;
    private PlayerTank playerTank;

    public static PlayerTankManager getInstance() {
        if (playerTankManager == null)
            playerTankManager = new PlayerTankManager(BulletFactory.getInstance());

        return playerTankManager;
    }

    private boolean isRideLooping = false;

    private PlayerTankManager(BulletFactory bulletFactory) {
        this.bulletFactory = bulletFactory;
        generateTanks();
    }

    private void generateTanks() {
        playerTank = new PlayerTank(bulletFactory);
        entityArr.add(playerTank);
    }

    public void shoot() {
        playerTank.shoot();
    }

    public void ride(Direction dir) {
        if (isRideLooping)
            ride(dir);
    }

    public Direction getDir() {
        return playerTank.getDir();
    }

    public boolean isRideLooping() {
        return isRideLooping;
    }

    public void setRideLooping(boolean rideLooping) {
        this.isRideLooping = rideLooping;
    }

    @Override
    public void update() {
        if (isRideLooping)
            ride(playerTank.getDir());
    }

}
