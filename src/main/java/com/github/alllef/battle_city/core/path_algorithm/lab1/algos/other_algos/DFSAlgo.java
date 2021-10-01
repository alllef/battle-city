package com.github.alllef.battle_city.core.path_algorithm.lab1.algos.other_algos;

import com.github.alllef.battle_city.core.game_entity.GameEntity;
import com.github.alllef.battle_city.core.path_algorithm.lab1.algos.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class DFSAlgo extends PathAlgo {

    Stack<Coords> coordsStack = new Stack<>();

    public DFSAlgo(GameEntity startEntity, GameEntity endEntity) {
        super(startEntity, endEntity);
    }

    @Override
    public List<Coords> createAlgo() {
        Coords firstCoord = getFirstVertex();
        coordsStack.add(firstCoord);

        if (firstCoord == null)
            return new ArrayList<>();

        while (!coordsStack.empty()) {

            if (nextVertex(coordsStack.peek()))
                return getPath(firstCoord);

            coordsStack.pop();
        }

        return new ArrayList<>();
    }

    public boolean nextVertex(Coords coords) {
        List<Coords> adjacentVertices = getPossibleAdjacentVertices(coords);

        while (!adjacentVertices.isEmpty()) {
            if (isMatrixPart(coordsStack.peek()))
                return true;
            adjacentVertices.forEach(coordsStack::push);
            adjacentVertices = getPossibleAdjacentVertices(coordsStack.peek());
        }

        return false;
    }

    @Override
    protected List<Coords> getPossibleAdjacentVertices(Coords coords) {
        List<Boolean> conditions = List.of(coords.y() < worldSize - 1, coords.y() > 0, coords.x() < worldSize - 1, coords.x() > 0);
        List<Coords> adjacentVertices = List.of(new Coords(coords.x(), coords.y() + 1), new Coords(coords.x(), coords.y() - 1), new Coords(coords.x() + 1, coords.y()), new Coords(coords.x() - 1, coords.y()));

        for (int i = 0; i < conditions.size(); i++) {
            int rand = i;
            Optional<Coords> coords1 = getAdjacentCoord(conditions.get(rand), adjacentVertices.get(rand));
            if (coords1.isPresent())
                return List.of(coords1.get());
        }

        return new ArrayList<>();
    }


    public List<Coords> getPath(Coords first) {
        List<Coords> path = new ArrayList<>();
        while (!coordsStack.peek().equals(first))
            path.add(coordsStack.pop());
        return path;
    }

}

