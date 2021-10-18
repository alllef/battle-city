package com.github.alllef.battle_city.core.input_handling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.path_algorithm.AlgoType;
import com.github.alllef.battle_city.core.path_algorithm.TankManipulation;

public class MainScreenInputAdapter extends InputAdapter {
    private final TankManipulation tankManipulation = TankManipulation.INSTANCE;
    Preferences prefs = Gdx.app.getPreferences("com.github.alllef.battle_city.prefs");

    @Override
    public boolean keyDown(int keycode) {
        if (keycode== Input.Keys.Z){
            tankManipulation.setAlgoType(chooseAlgoType());
            return true;
        }
        return false;
    }

    private AlgoType chooseAlgoType(){
        int labNum = prefs.getInteger("lab_number");
        if (labNum == 1)
            return chooseAlgoTypeLab1();
        if (labNum == 2)
            return chooseAlgoTypeLab2();

        return AlgoType.BFS;
    }

    private AlgoType chooseAlgoTypeLab1() {
        AlgoType[] algos = AlgoType.values();
        int currentAlgo = tankManipulation.getAlgoType().ordinal();
        if (currentAlgo + 1 < 3)
            return algos[currentAlgo + 1];

        return algos[0];
    }

    private AlgoType chooseAlgoTypeLab2() {
        AlgoType[] algos = AlgoType.values();
        int currentAlgo = tankManipulation.getAlgoType().ordinal();
        if (currentAlgo + 1 < algos.length)
            return algos[currentAlgo + 1];

        return algos[3];
    }
}
