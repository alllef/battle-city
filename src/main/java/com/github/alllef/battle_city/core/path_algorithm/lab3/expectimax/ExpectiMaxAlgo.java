package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.AlgoType;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab2.AStarAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.NodeType;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ChanceNode;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ChanceType;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.UtilityNode;
import com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta.MiniMaxNode;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.*;
import java.util.stream.Collectors;

public class ExpectiMaxAlgo {
    RTreeMap rTreeMap = RTreeMap.getInstance();
    UtilityNode minimaxTree;
    Rectangle start;
    Rectangle end;
    Direction dir;

    public ExpectiMaxAlgo(Rectangle start, Rectangle end, Direction dir) {
        this.start = start;
        this.end = end;
        this.dir = dir;
    }

    public List<UtilityNode> getChanceChildren(UtilityNode parent, int depth, ChanceType type) {
        Rectangle parRect = parent.getRect();

        if (depth == 0) {
            parent.setCostFunc(calcFunc(parent.rect, end));
            return new ArrayList<>();
        }

        List<Map.Entry<Direction,Rectangle>> children;
        Direction[] directions = Direction.values();

        children = Arrays.stream(directions)
                .map(dir -> switch (dir) {
                    case LEFT -> Map.entry(dir, new Coords((int) parRect.getX() - 1, (int) parRect.getY()));
                    case RIGHT -> Map.entry(dir, new Coords((int) parRect.getX() + (int) parRect.getWidth() + 1, (int) parRect.getY()));
                    case UP -> Map.entry(dir, new Coords((int) parRect.getX(), (int) parRect.getY() + (int) parRect.getHeight() + 1));
                    case DOWN -> Map.entry(dir, new Coords((int) parRect.getX(), (int) parRect.getY() - 1));

                })
                .filter(entry -> rTreeMap.isEmpty(entry.getValue()))
                .filter(entry -> {
                    boolean result = isToEndDirection(entry.getKey(), entry.getValue());
                    if (type == ChanceType.TO_TANK)
                        return result;
                    return !result;
                })
                .map(entry -> {
                    Coords coords = entry.getValue();
                    Map.Entry<Direction, Rectangle> rectangleEntry = null;
                    switch (entry.getKey()) {
                        case LEFT, DOWN -> rectangleEntry = Map.entry(entry.getKey(), new Rectangle(coords.x(), coords.y(), parRect.getWidth(), parRect.getHeight()));
                        case RIGHT -> rectangleEntry = Map.entry(entry.getKey(), new Rectangle(coords.x() - parRect.getWidth(), coords.y(), parRect.getWidth(), parRect.getHeight()));
                        case UP -> rectangleEntry = Map.entry(entry.getKey(), new Rectangle(coords.x(), coords.y() - parRect.getHeight(), parRect.getWidth(), parRect.getHeight()));
                    }
                    return rectangleEntry;
                })
                .collect(Collectors.toList());

        ChanceNode chance = new ChanceNode(new ArrayList<>(),parent,children,,type);
        return children;
    }

    public boolean isToEndDirection(Direction dir, Coords startCoords) {
        Coords endCoords = new Coords((int) end.getX(), (int) end.getY());
        boolean result = false;

        switch (dir) {
            case LEFT -> result = endCoords.x() <= startCoords.x();
            case RIGHT -> result = endCoords.x() >= endCoords.x();
            case UP -> result = endCoords.y() >= endCoords.y();
            case DOWN -> result = endCoords.y() <= endCoords.y();
        }

        return result;
    }

    public int calcFunc(Rectangle start, Rectangle end) {
        PathAlgo algo = new AStarAlgo(start, end, AlgoType.ASTAR_COORDS);
        List<Coords> result = algo.startAlgo();
        if (result.size() == 0)
            return Integer.MAX_VALUE;

        return result.size();
    }

}
