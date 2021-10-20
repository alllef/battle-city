package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.lab3.MiniMaxAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.NodeType;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ChanceNode;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ChanceType;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.ExpectiMaxNode;
import com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node.UtilityNode;
import com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta.MiniMaxNode;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.*;
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

    public Direction startAlgo(int depth) {
        minimaxTree = new UtilityNode(NodeType.MIN, 0.0f, new ArrayList<>(), null, dir, start);
        List<Optional<ChanceNode>> tmpChanceNode = List.of(getChanceChild(minimaxTree, depth, ChanceType.TO_TANK), getChanceChild(minimaxTree, depth, ChanceType.FROM_TANK));
        tmpChanceNode.forEach(optChild -> {
            optChild.ifPresent(chanceNode -> minimaxTree.addChild(chanceNode));
        });

        expectiMaxAlgo(depth);

        return null;
    }

    public void expectiMaxAlgo(int depth) {
        Stack<ExpectiMaxNode> stack = new Stack<>();
        ExpectiMaxNode node = minimaxTree;

        stack.push(node);
        node.setTraversed(true);

        while (!stack.isEmpty()) {
            Optional<ExpectiMaxNode> unusedChild = getUnusedChild(stack.peek());

            while (unusedChild.isPresent()) {
                ExpectiMaxNode child = unusedChild.get();
                stack.push(child);
                unusedChild = getUnusedChild(stack.peek());
            }

            ExpectiMaxNode child = stack.pop();
            if (stack.isEmpty()) {
                System.out.println("ok");
                System.out.println("");
                return;
            }

            ExpectiMaxNode parent = stack.pop();

            parent.calcResultFunc();
        }

    }

    public Optional<ChanceNode> getChanceChild(UtilityNode parent, int depth, ChanceType type) {
        Rectangle parRect = parent.getRect();

        List<Map.Entry<Direction, Rectangle>> children;
        Direction[] directions = Direction.values();

        children = Arrays.stream(directions)
                .map(dir -> getNearestCoord(dir, parRect))
                .filter(entry -> rTreeMap.isEmpty(entry.getValue()))
                .filter(entry -> {
                    boolean result = isToEndDirection(entry.getKey(), entry.getValue());
                    if (type == ChanceType.TO_TANK)
                        return result;
                    return !result;
                })
                .map(entry -> mapNearCoordsToRect(entry.getKey(), entry.getValue(), parRect))
                .collect(Collectors.toList());

        ChanceNode chance = new ChanceNode(new ArrayList<>(), parent, children, type);
        chance.setChildren(getUtilityChildren(chance, depth - 1));

        if (chance.getChildren().isEmpty())
            return Optional.empty();
        return Optional.of(chance);
    }

    public List<UtilityNode> getUtilityChildren(ChanceNode parent, int depth) {
        List<UtilityNode> children;

        children = parent.getPossibleChildren().stream()
                .map(entry -> {
                    UtilityNode utility = new UtilityNode(parent.getParent().getType(), getPercentage(parent.getType()), new ArrayList<>(), parent, entry.getKey(), entry.getValue());
                    if (depth == 0)
                        utility.setCostFunc(calcLeafFunc(utility.getRect(), end));
                    else {
                        List<Optional<ChanceNode>> tmpChanceNode = List.of(getChanceChild(utility, depth, ChanceType.TO_TANK), getChanceChild(utility, depth, ChanceType.FROM_TANK));
                        tmpChanceNode.forEach(optChild -> {
                            optChild.ifPresent(utility::addChild);
                        });

                    }
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

    private Optional<ExpectiMaxNode> getUnusedChild(ExpectiMaxNode node) {
        Optional<ExpectiMaxNode> child = Optional.empty();
        if (node instanceof UtilityNode utility) {
            for (ChanceNode tmpChild : utility.getChildren()) {
                if (!tmpChild.isTraversed()) {
                    tmpChild.setTraversed(true);
                    return Optional.of(tmpChild);
                }
            }
        } else if (node instanceof ChanceNode chance) {
            for (UtilityNode tmpChild : chance.getChildren()) {
                if (!tmpChild.isTraversed()) {
                    tmpChild.setTraversed(true);
                    return Optional.of(tmpChild);
                }
            }
        }
        return child;
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
