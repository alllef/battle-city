package com.github.alllef.battle_city.core.input_handling;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.github.alllef.battle_city.core.path_algorithm.TankManipulation;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.AlgoType;

public class MainScreenInputAdapter extends InputAdapter {
    private final TankManipulation tankManipulation = TankManipulation.INSTANCE;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode== Input.Keys.Z){
            tankManipulation.setAlgoType(chooseAlgoType());
            return true;
        }

        return false;
    }

    private AlgoType chooseAlgoType() {
        AlgoType[] algos = AlgoType.values();
        int currentAlgo = tankManipulation.getAlgoType().ordinal();
        if (currentAlgo + 1 < algos.length)
            return algos[currentAlgo + 1];

        return algos[0];
    }
}
