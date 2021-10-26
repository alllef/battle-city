package com.github.alllef.battle_city.core.game_entity.tank.player;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.EntityManager;
import com.github.alllef.battle_city.core.util.enums.Direction;

public class PlayerTankManager extends EntityManager<PlayerTank> {
    private BulletFactory bulletFactory;
    private PlayerTank playerTank;

    private boolean isRideLooping = false;

    public PlayerTankManager(BulletFactory bulletFactory, Preferences prefs) {
        super(prefs);
        this.bulletFactory = bulletFactory;
        generateTanks();
    }

    private void generateTanks() {
        playerTank = new PlayerTank(bulletFactory, prefs);
        entityArr.add(playerTank);
    }

    public void shoot() {
        playerTank.shoot();
    }

    public void ride(Direction dir) {
        playerTank.ride(dir);
    }


    @Override
    public void update() {
        if (isRideLooping)
            ride(playerTank.getDir());
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

    public Sprite getSprite() {
        return playerTank.getSprite();
    }
}
