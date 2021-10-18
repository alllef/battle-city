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

    public void startAlgo() {
        minimaxTree = new MiniMaxNode(0.0f, null, null, dir, start, MiniMaxNode.NodeType.MIN);
        minimaxTree.setChildren(getChildren(minimaxTree, 3));
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
                    MiniMaxNode node = new MiniMaxNode(0.0f, parent, null, rectEntry.getKey(), rectEntry.getValue(), MiniMaxNode.NodeType.values()[parent.type.ordinal() % 2]);
                    node.setChildren(getChildren(node, depth - 1));
                    return node;
                })
                .collect(Collectors.toList());

        return children;
    }

    public float calcFunc(Rectangle start, Rectangle end) {
        PathAlgo algo = new AStarAlgo(start, end, AlgoType.ASTAR_COORDS);
        List<Coords> result = algo.startAlgo();
        if (result.size() == 0)
            return Float.MAX_VALUE;
        return result.size();
    }
}
