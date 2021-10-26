package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.lab3.MiniMaxAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.NodeType;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ChanceNode;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ChanceType;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ExpectiMaxNode;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.UtilityNode;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.alllef.battle_city.core.path_algorithm.lab3.NodeType.MAX;
import static com.github.alllef.battle_city.core.path_algorithm.lab3.NodeType.MIN;

public class ExpectiMaxAlgo extends MiniMaxAlgo<ExpectiMaxNode> {

    UtilityNode minimaxTree;

    public ExpectiMaxAlgo(RTreeMap rTreeMap, Rectangle start, Rectangle end, Direction dir) {
        super(rTreeMap, start, end, dir);
    }

    public Direction startAlgo(int depth) {
        minimaxTree = new UtilityNode(MIN, 1.0f, new ArrayList<>(), null, dir, start);
        var tmpChanceNode = List.of(getChanceChild(minimaxTree, depth, ChanceType.TO_TANK), getChanceChild(minimaxTree, depth, ChanceType.FROM_TANK));
        tmpChanceNode.forEach(optChild -> optChild.ifPresent(chanceNode -> minimaxTree.addChild(chanceNode)));

        return expectiMaxAlgo();
    }

    public Direction expectiMaxAlgo() {
        Stack<ExpectiMaxNode> stack = new Stack<>();
        ExpectiMaxNode node = minimaxTree;

        stack.push(node);
        node.setTraversed(true);

        while (!stack.isEmpty()) {
            Optional<ExpectiMaxNode> unusedChild = getUnusedChild(stack.peek().getChildren());

            while (unusedChild.isPresent()) {
                ExpectiMaxNode child = unusedChild.get();
                stack.push(child);
                unusedChild = getUnusedChild(stack.peek().getChildren());
            }
            stack.pop();

            if (stack.isEmpty())
                return getFinalResult();

            ExpectiMaxNode parent = stack.peek();
            parent.calcResultFunc();
        }

        return null;
    }

    public Optional<ChanceNode> getChanceChild(UtilityNode parent, int depth, ChanceType type) {
        Rectangle parRect = parent.getRect();

        List<Map.Entry<Direction, Rectangle>> children;
        Direction[] directions = Direction.values();

        children = Arrays.stream(directions)
                .filter(dir -> {
                    boolean result = isToEndDirection(dir, new Coords((int) parRect.getX(), (int) parRect.getY()));
                    if (type == ChanceType.TO_TANK)
                        return result;
                    return !result;
                })
                .map(dir -> getNearestCoord(dir, parRect))
                .filter(entry -> rTreeMap.isEmpty(entry.getValue()))
                .map(entry -> mapNearCoordsToRect(entry.getKey(), entry.getValue(), parRect))
                .collect(Collectors.toList());

        ChanceNode chance = new ChanceNode(new ArrayList<>(), parent, children, type);
        chance.setChildren(getUtilityChildren(chance, depth - 1));

        if (chance.getChildren().isEmpty())
            return Optional.empty();

        return Optional.of(chance);
    }

    public List<ExpectiMaxNode> getUtilityChildren(ChanceNode parent, int depth) {
        List<ExpectiMaxNode> children;

        children = parent.getPossibleChildren().stream()
                .map(entry -> {
                    UtilityNode utility = new UtilityNode(parent.getParent().getType(), getPercentage(parent.getType()), new ArrayList<>(), parent, entry.getKey(), entry.getValue());
                    if (depth == 0)
                        utility.setCostFunc(calcLeafFunc(utility.getRect(), end));
                    else {
                        var tmpChanceNode = List.of(getChanceChild(utility, depth, ChanceType.TO_TANK), getChanceChild(utility, depth, ChanceType.FROM_TANK));
                        tmpChanceNode.forEach(optChild -> optChild.ifPresent(utility::addChild));

                    }
                    return utility;
                })
                .collect(Collectors.toList());

        return children;
    }

    public float getPercentage(ChanceType type) {
        if (type == ChanceType.TO_TANK)
            return 0.33f;

        return 0.5f;
    }

    public boolean isToEndDirection(Direction dir, Coords startCoords) {
        Coords endCoords = new Coords((int) end.getX(), (int) end.getY());
        boolean result = false;

        switch (dir) {
            case LEFT -> result = endCoords.x() < startCoords.x();
            case RIGHT -> result = endCoords.x() > startCoords.x();
            case UP -> result = endCoords.y() > startCoords.y();
            case DOWN -> result = endCoords.y() < startCoords.y();
        }

        return result;
    }

    private Direction getFinalResult() {
        for (ExpectiMaxNode child : minimaxTree.getChildren()) {
            if (Float.compare(child.getCostFunc(), minimaxTree.getCostFunc()) == 0)
                return maxOrMin(child);
            if (child instanceof ChanceNode chanceNode)
                return chanceNode.getPossibleChildren().get(new Random().nextInt(chanceNode.getPossibleChildren().size())).getKey();

        }
        System.out.println("Did not found");
        return Direction.values()[new Random().nextInt(Direction.values().length)];
    }

    private Direction maxOrMin(ExpectiMaxNode node) {
        float resultFunc = node.getChildren().get(0).getCostFunc();
        List<Direction> possibleDirections = new ArrayList<>();
        if (node instanceof ChanceNode chanceNode) {

            for (ExpectiMaxNode child : node.getChildren()) {
                switch (chanceNode.getParent().getType()) {
                    case MIN -> resultFunc = Math.min(resultFunc, child.getCostFunc());
                    case MAX -> resultFunc = Math.max(resultFunc, child.getCostFunc());
                }
            }

            for (ExpectiMaxNode child : node.getChildren()) {
                if (Float.compare(child.getCostFunc(), resultFunc) == 0)
                    if (child instanceof UtilityNode utilityNode)
                        possibleDirections.add(utilityNode.getDir());
            }
        }

        return possibleDirections.get(new Random().nextInt(possibleDirections.size()));
    }
}
