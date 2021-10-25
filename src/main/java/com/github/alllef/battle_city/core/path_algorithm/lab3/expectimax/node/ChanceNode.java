package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.util.enums.Direction;


import java.util.List;
import java.util.Map;

public class ChanceNode extends ExpectiMaxNode {
    protected UtilityNode parent;
    List<Map.Entry<Direction, Rectangle>> possibleChildren;
    ChanceType type;

    public ChanceNode(List<ExpectiMaxNode> children, UtilityNode parent, List<Map.Entry<Direction,Rectangle>> possibleChildren, ChanceType type) {
        super(children);
        this.parent = parent;
        this.possibleChildren = possibleChildren;
        this.type = type;
    }

    @Override
    public void calcResultFunc() {
        float result = 0.0f;
        for (ExpectiMaxNode child : children)
            result += child.getCostFunc();

        this.setCostFunc(result);
    }

    public UtilityNode getParent() {
        return parent;
    }

    public void setParent(UtilityNode parent) {
        this.parent = parent;
    }

    public void addPossibleChild(Map.Entry<Direction,Rectangle> child){
        possibleChildren.add(child);
    }

    public List<Map.Entry<Direction, Rectangle>> getPossibleChildren() {
        return possibleChildren;
    }

    public void setPossibleChildren(List<Map.Entry<Direction, Rectangle>> possibleChildren) {
        this.possibleChildren = possibleChildren;
    }

    public ChanceType getType() {
        return type;
    }

    public void setType(ChanceType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChanceNode)) return false;
        if (!super.equals(o)) return false;

        ChanceNode that = (ChanceNode) o;

        if (getPossibleChildren() != null ? !getPossibleChildren().equals(that.getPossibleChildren()) : that.getPossibleChildren() != null)
            return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getPossibleChildren() != null ? getPossibleChildren().hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
