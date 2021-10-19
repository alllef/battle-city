package com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.AlgoType;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.path_algorithm.algos.lab2.AStarAlgo;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.*;
import java.util.stream.Collectors;

public class MiniMaxAlphaBetaAlgo {
    RTreeMap rTreeMap = RTreeMap.getInstance();
    MiniMaxNode minimaxTree;
    Rectangle start;
    Rectangle end;
    Direction dir;

    public MiniMaxAlphaBetaAlgo(Rectangle start, Rectangle end, Direction dir) {
        this.start = start;
        this.end = end;
        this.dir = dir;
    }

    public Direction startAlgo(int depth) {
        minimaxTree = new MiniMaxNode(0, null, null, dir, start, MiniMaxNode.NodeType.MIN);
        minimaxTree.setChildren(getChildren(minimaxTree, depth));

        return alphaBetaAlgo();
    }

    public Direction alphaBetaAlgo() {
        Stack<MiniMaxNode> stack = new Stack<>();
        MiniMaxNode node = minimaxTree;

        stack.push(node);
        node.setTraversed(true);

        while (!stack.isEmpty()) {
            Optional<MiniMaxNode> unusedChild = getUnusedChild(stack.peek());

            while (unusedChild.isPresent()) {
                MiniMaxNode child = unusedChild.get();
                child.beta = stack.peek().beta;
                child.alpha = stack.peek().alpha;
                stack.push(child);
                unusedChild = getUnusedChild(stack.peek());
            }

            MiniMaxNode child = stack.pop();
            if (stack.isEmpty()) {
                for (MiniMaxNode child1 : child.children) {
                    if (child1.costFunc == child.costFunc)
                        return child1.dir;
                }
            }

            if (stack.isEmpty()){
                return null;
            }
                MiniMaxNode parent = stack.peek();

            if (parent.type == MiniMaxNode.NodeType.MAX) {
                parent.alpha = Math.max(parent.alpha, child.costFunc);
                parent.costFunc = parent.alpha;

                if (parent.beta <= parent.alpha) {
                    parent.parent.beta = Math.min(parent.parent.alpha, parent.costFunc);
                    parent.parent.costFunc = parent.parent.beta;
                    stack.pop();
                }

            } else if (parent.type == MiniMaxNode.NodeType.MIN) {
                parent.beta = Math.min(parent.beta, stack.peek().costFunc);
                parent.costFunc = parent.beta;

                if (parent.beta <= parent.alpha) {
                    parent.parent.alpha = Math.max(parent.parent.beta, parent.costFunc);
                    parent.parent.costFunc = parent.parent.alpha;
                    stack.pop();
                }

            }
        }

        return null;
    }

    private Optional<MiniMaxNode> getUnusedChild(MiniMaxNode node) {
        Optional<MiniMaxNode> child = Optional.empty();

        for (MiniMaxNode tmpChild : node.children) {
            if (!tmpChild.isTraversed()) {
                tmpChild.setTraversed(true);
                return Optional.of(tmpChild);
            }
        }
        return child;
    }

    public List<MiniMaxNode> getChildren(MiniMaxNode parent, int depth) {
        Rectangle parRect = parent.rect;

        if (depth == 0) {
            parent.setCostFunc(calcFunc(parent.rect, end));
            return new ArrayList<>();
        }

        List<MiniMaxNode> children;
        Direction[] directions = Direction.values();

        children = Arrays.stream(directions)
                .map(dir -> switch (dir) {
                    case LEFT -> Map.entry(dir, new Coords((int) parRect.getX() - 1, (int) parRect.getY()));
                    case RIGHT -> Map.entry(dir, new Coords((int) parRect.getX() + (int) parRect.getWidth() + 1, (int) parRect.getY()));
                    case UP -> Map.entry(dir, new Coords((int) parRect.getX(), (int) parRect.getY() + (int) parRect.getHeight() + 1));
                    case DOWN -> Map.entry(dir, new Coords((int) parRect.getX(), (int) parRect.getY() - 1));

                })
                .filter(entry -> rTreeMap.isEmpty(entry.getValue()))
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
                .map(rectEntry -> {
                    MiniMaxNode node = new MiniMaxNode(0, parent, null, rectEntry.getKey(), rectEntry.getValue(), MiniMaxNode.NodeType.chooseType(parent.type));
                    node.setChildren(getChildren(node, depth - 1));
                    return node;
                })
                .collect(Collectors.toList());

        return children;
    }

    public int calcFunc(Rectangle start, Rectangle end) {
        PathAlgo algo = new AStarAlgo(start, end, AlgoType.ASTAR_COORDS);
        List<Coords> result = algo.startAlgo();
        if (result.size() == 0) {
            System.out.println("Max size");
            return Integer.MAX_VALUE;
        }
        System.out.println("size" + result.size());
        return result.size();
    }
}
