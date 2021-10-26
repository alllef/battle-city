package com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.lab3.MiniMaxAlgo;
import com.github.alllef.battle_city.core.path_algorithm.lab3.NodeType;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;

import java.util.*;
import java.util.stream.Collectors;

public class MiniMaxAlphaBetaAlgo extends MiniMaxAlgo<AlphaBetaNode> {
    RTreeMap rTreeMap;
    AlphaBetaNode minimaxTree;

    public MiniMaxAlphaBetaAlgo(RTreeMap rTreeMap,Rectangle start, Rectangle end, Direction dir) {
        super(rTreeMap,start, end, dir);
    }

    public Direction startAlgo(int depth) {
        minimaxTree = new AlphaBetaNode(0, null, null, dir, start, NodeType.MIN);
        minimaxTree.setChildren(getChildren(minimaxTree, depth));

        return alphaBetaAlgo();
    }

    public Direction alphaBetaAlgo() {
        Stack<AlphaBetaNode> stack = new Stack<>();
        AlphaBetaNode node = minimaxTree;

        stack.push(node);
        node.setTraversed(true);

        while (!stack.isEmpty()) {
            Optional<AlphaBetaNode> unusedChild = getUnusedChild(stack.peek().children);

            while (unusedChild.isPresent()) {
                AlphaBetaNode child = unusedChild.get();
                child.beta = stack.peek().beta;
                child.alpha = stack.peek().alpha;
                stack.push(child);
                unusedChild = getUnusedChild(stack.peek().children);
            }

            AlphaBetaNode child = stack.pop();

            if (stack.isEmpty()) {
                for (AlphaBetaNode child1 : child.children) {
                    if (child1.costFunc == child.costFunc)
                        return child1.dir;
                }
            }

            AlphaBetaNode parent = stack.peek();
            switch (parent.type) {
                case MAX -> {
                    maximize(child);

                    if (parent.beta <= parent.alpha) {
                        minimize(parent);
                        stack.pop();
                    }
                }

                case MIN -> {
                    minimize(child);
                    if (parent.beta <= parent.alpha) {
                        maximize(parent);
                        stack.pop();
                    }
                }
            }

        }
        return null;
    }

    private void maximize(AlphaBetaNode child) {
        AlphaBetaNode parent = child.parent;
        parent.alpha = Math.max(parent.alpha, child.costFunc);
        parent.costFunc = parent.alpha;
    }

    private void minimize(AlphaBetaNode child) {
        AlphaBetaNode parent = child.parent;
        parent.beta = Math.min(parent.beta, child.costFunc);
        parent.costFunc = parent.beta;
    }


    public List<AlphaBetaNode> getChildren(AlphaBetaNode parent, int depth) {
        Rectangle parRect = parent.rect;

        if (depth == 0) {
            parent.setCostFunc(calcLeafFunc(parent.rect, end));
            return new ArrayList<>();
        }

        List<AlphaBetaNode> children;
        Direction[] directions = Direction.values();

        children = Arrays.stream(directions)
                .map(dir -> getNearestCoord(dir, parRect))
                .filter(entry -> rTreeMap.isEmpty(entry.getValue()))
                .map(entry -> mapNearCoordsToRect(entry.getKey(), entry.getValue(), parRect))
                .map(rectEntry -> {
                    AlphaBetaNode node = new AlphaBetaNode(0, parent, null, rectEntry.getKey(), rectEntry.getValue(), NodeType.chooseType(parent.type));
                    node.setChildren(getChildren(node, depth - 1));
                    return node;
                })
                .collect(Collectors.toList());

        return children;
    }

}
