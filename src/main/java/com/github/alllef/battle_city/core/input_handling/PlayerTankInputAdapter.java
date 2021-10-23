package com.github.alllef.battle_city.core.input_handling;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTankManager;
import com.github.alllef.battle_city.core.util.Direction;

import java.util.Optional;

public class PlayerTankInputAdapter extends InputAdapter {
    PlayerTankManager playerTankManager = PlayerTankManager.getInstance();

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            playerTankManager.shoot();
            return true;
        }

        Optional<Direction> optionalDir = Direction.of(keycode);

        if (optionalDir.isPresent()) {
            playerTankManager.setRideLooping(true);
            playerTankManager.ride(optionalDir.get());
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Optional<Direction> dir = Direction.of(keycode);
        if (dir.isPresent() && dir.get() == playerTankManager.getDir()) {
            playerTankManager.setRideLooping(false);
            return true;
        }

        return false;
    }
}
