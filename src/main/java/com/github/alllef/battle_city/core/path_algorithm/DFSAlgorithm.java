package com.github.alllef.battle_city.core.path_algorithm;

import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class DFSAlgorithm extends PathAlgo {

    Stack<Coords> coordsStack = new Stack<Coords>();
    Map<Coords, Boolean> coordsBooleanMap = new HashMap<>();

    @Override
    public List<Coords> createAlgo() {
        coordsStack.add(firstNode);
        return nextVertex(coordsStack.peek());
        coordsStack.
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
            else return null;
        }

        return null;
    }

}
