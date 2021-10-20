package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.lab3.MiniMaxAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ChanceNode;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ChanceType;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.UtilityNode;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpectiMaxAlgo implements MiniMaxAlgo {
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

    public ChanceNode getChanceChild(UtilityNode parent, int depth, ChanceType type) {
        Rectangle parRect = parent.getRect();

        List<Map.Entry<Direction, Rectangle>> children;
        Direction[] directions = Direction.values();

        children = Arrays.stream(directions)
                .map(dir -> getNearestCoord(dir,parRect))
                .filter(entry -> rTreeMap.isEmpty(entry.getValue()))
                .filter(entry -> {
                    boolean result = isToEndDirection(entry.getKey(), entry.getValue());
                    if (type == ChanceType.TO_TANK)
                        return result;
                    return !result;
                })
                .map(entry -> mapNearCoordsToRect(entry.getKey(),entry.getValue(),parRect))
                .collect(Collectors.toList());

        ChanceNode chance = new ChanceNode(new ArrayList<>(), parent, children, type);
        chance.setChildren(getUtilityChildren(chance, depth - 1));
        return chance;
    }

    public List<UtilityNode> getUtilityChildren(ChanceNode parent, int depth) {
        List<UtilityNode> children;

        children = parent.getPossibleChildren().stream()
                .map(entry -> {
                    UtilityNode utility = new UtilityNode(parent.getParent().getType(), getPercentage(parent.getType()), new ArrayList<>(), parent, entry.getKey(), entry.getValue());
                    if (depth == 0)
                        utility.setCostFunc(calcLeafFunc(utility.getRect(), end));
                    else
                        utility.setChildren(List.of(getChanceChild(utility, depth, ChanceType.TO_TANK), getChanceChild(utility, depth, ChanceType.FROM_TANK)));
                    return utility;
                })
                .collect(Collectors.toList());

        return children;
    }

    public float getPercentage(ChanceType type) {
        if (type == ChanceType.TO_TANK)
            return 0.5f;

        return 0.33f;
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
}
