package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node;

import com.github.alllef.battle_city.core.util.Direction;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class ChanceNode extends ExpectiMaxNode {

    private List<UtilityNode> children;
    protected UtilityNode parent;
    Map<Direction, Rectangle> possibleChildren;
    ChanceType type;

    public ChanceNode(List<UtilityNode> children, UtilityNode parent, Map<Direction, Rectangle> possibleChildren, ChanceType type) {
        this.children = children;
        this.parent = parent;
        this.possibleChildren = possibleChildren;
        this.type = type;
    }

    @Override
    public void calcResultFunc() {
        float result = 0.0f;
        for (UtilityNode child : children)
            result += child.getCostFunc() * child.getPercentage();

        this.setCostFunc(result);
    }

    public List<UtilityNode> getChildren() {
        return children;
    }

    public void setChildren(List<UtilityNode> children) {
        this.children = children;
    }

    public UtilityNode getParent() {
        return parent;
    }

    public void setParent(UtilityNode parent) {
        this.parent = parent;
    }

    public void addPossibleChild(Map.Entry<Direction,Rectangle> child){
        possibleChildren.put(child.getKey(),child.getValue());
    }

    @Override
    public String toString() {
        return "ChanceNode{" +
                "children=" + children +
                ", parent=" + parent +
                '}';
    }
}
