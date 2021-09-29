package com.github.alllef.battle_city.core.path_algorithm.lab1;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class DFSAlgo extends PathAlgo {

    Stack<Coords> coordsStack = new Stack<Coords>();
    Map<Coords, Boolean> coordsBooleanMap = new HashMap<>();

    public DFSAlgo(Rectangle startRect, Rectangle endRect) {
        super(startRect, endRect);
    }

    @Override
    public List<Coords> createAlgo() {
        coordsStack.add(getFirstVertix());
        if (getFirstVertix() == null)
            return new ArrayList<>();

        return nextVertex(coordsStack.peek());

    }

    public List<Coords> nextVertex(Coords coords) {
        List<Coords> adjacentVertices = getAdjacentVertices(coords);
        boolean isEnd = true;

        for (Coords adjacentVertix : adjacentVertices) {
            if (!coordsBooleanMap.get(adjacentVertix)) {
                coordsBooleanMap.put(adjacentVertix, true);
                coordsStack.push(adjacentVertix);
                isEnd = false;
                break;
            }
        }

        if (isMatrixPart(coordsStack.peek()))
            return coordsStack;

        if (isEnd) {
            coordsStack.pop();
            if (!coordsStack.isEmpty()) nextVertex(coordsStack.peek());
            else return new ArrayList<>();
        }

        return new ArrayList<>();
    }

}
