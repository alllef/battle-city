package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.lab3.NodeType;
import com.github.alllef.battle_city.core.util.Direction;

import java.util.List;

public class UtilityNode extends ExpectiMaxNode {
    NodeType type;
    protected float percentage;
    protected List<ChanceNode> children;
    protected ChanceNode parent;
    protected Direction dir;
    protected Rectangle rect;

    public UtilityNode(NodeType type, float percentage, List<ChanceNode> children, ChanceNode parent, Direction dir, Rectangle rect) {
        this.type = type;
        this.percentage = percentage;
        this.children = children;
        this.parent = parent;
        this.dir = dir;
        this.rect = rect;
    }

    @Override
    public void calcResultFunc() {
        switch (type) {
            case MAX -> children.forEach(child -> setCostFunc(Math.max(child.getCostFunc(), getCostFunc())));
            case MIN -> children.forEach(child -> setCostFunc(Math.min(child.getCostFunc(), getCostFunc())));
        }
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public List<ChanceNode> getChildren() {
        return children;
    }

    public void addChild(ChanceNode chanceNode){
        children.add(chanceNode);
    }

    public ChanceNode getParent() {
        return parent;
    }

    public void setParent(ChanceNode parent) {
        this.parent = parent;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void setChildren(List<ChanceNode> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UtilityNode)) return false;
        if (!super.equals(o)) return false;

        UtilityNode that = (UtilityNode) o;

        if (Float.compare(that.getPercentage(), getPercentage()) != 0) return false;
        if (type != that.type) return false;
        if (dir != that.dir) return false;
        return rect != null ? rect.equals(that.rect) : that.rect == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (getPercentage() != +0.0f ? Float.floatToIntBits(getPercentage()) : 0);
        result = 31 * result + (dir != null ? dir.hashCode() : 0);
        result = 31 * result + (rect != null ? rect.hashCode() : 0);
        return result;
    }
}
