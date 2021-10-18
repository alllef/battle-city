package com.github.alllef.battle_city.core.path_algorithm.algos.lab1.other_algos;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class DFSAlgo extends PathAlgo<Stack<Coords>> {
    public DFSAlgo(Rectangle startRect, Rectangle endRect) {
        super(startRect, endRect);
        collection = new Stack<>();
    }

    @Override
    public List<Coords> startAlgo() {
        Coords firstCoord = getFirstVertex();
        collection.add(firstCoord);

        if (firstCoord == null)
            return new ArrayList<>();

        while (!collection.empty()) {

            if (nextVertex(collection.peek()))
                return getPath(firstCoord);

            collection.pop();
        }

        return new ArrayList<>();
    }

    public boolean nextVertex(Coords coords) {
        List<Coords> adjacentVertices = getPossibleAdjacentVertices(coords);

        while (!adjacentVertices.isEmpty()) {
            if (isMatrixPart(collection.peek()))
                return true;
            adjacentVertices.forEach(collection::push);
            adjacentVertices = getPossibleAdjacentVertices(collection.peek());
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
        while (!collection.peek().equals(first))
            path.add(collection.pop());
        return path;
    }

}

