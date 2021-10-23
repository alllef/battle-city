package com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.lab3.MiniMaxAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.NodeType;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.*;
import java.util.stream.Collectors;

public class MiniMaxAlphaBetaAlgo implements MiniMaxAlgo {
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
        minimaxTree = new MiniMaxNode(0, null, null, dir, start, NodeType.MIN);
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

            MiniMaxNode parent = stack.peek();

            if (parent.type == NodeType.MAX) {
                maximize(child);

                if (parent.beta <= parent.alpha) {
                    minimize(parent);
                    stack.pop();
                }

            } else if (parent.type == NodeType.MIN) {
                minimize(child);

                if (parent.beta <= parent.alpha) {
                    maximize(parent);
                    stack.pop();
                }
            }
        }
        return null;
    }

    private void maximize(MiniMaxNode child) {
        MiniMaxNode parent = child.parent;
        parent.alpha = Math.max(parent.alpha, child.costFunc);
        parent.costFunc = parent.alpha;
    }

    private void minimize(MiniMaxNode child) {
        MiniMaxNode parent = child.parent;
        parent.beta = Math.min(parent.beta, child.costFunc);
        parent.costFunc = parent.beta;
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
            parent.setCostFunc(calcLeafFunc(parent.rect, end));
            return new ArrayList<>();
        }

        List<MiniMaxNode> children;
        Direction[] directions = Direction.values();

        children = Arrays.stream(directions)
                .map(dir -> getNearestCoord(dir, parRect))
                .filter(entry -> rTreeMap.isEmpty(entry.getValue()))
                .map(entry -> mapNearCoordsToRect(entry.getKey(),entry.getValue(),parRect))
                .map(rectEntry -> {
                    MiniMaxNode node = new MiniMaxNode(0, parent, null, rectEntry.getKey(), rectEntry.getValue(), NodeType.chooseType(parent.type));
                    node.setChildren(getChildren(node, depth - 1));
                    return node;
                })
                .collect(Collectors.toList());

        return children;
    }

}
