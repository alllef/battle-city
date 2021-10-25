package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node;

import com.github.alllef.battle_city.core.path_algorithm.lab3.MiniMaxNode;

import java.util.List;

public abstract class ExpectiMaxNode extends MiniMaxNode {
    private float costFunc;
    protected List<ExpectiMaxNode> children;

    public ExpectiMaxNode(List<ExpectiMaxNode> children) {
        this.children = children;
    }

    public float getCostFunc() {
        return costFunc;
    }

    public void setCostFunc(float costFunc) {
        this.costFunc = costFunc;
    }


    public List<ExpectiMaxNode> getChildren() {
        return children;
    }

    public void setChildren(List<ExpectiMaxNode> children) {
        this.children = children;
    }

    public void addChild(ExpectiMaxNode child){
        children.add(child);
    }

    public abstract void calcResultFunc();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpectiMaxNode)) return false;

        ExpectiMaxNode that = (ExpectiMaxNode) o;

        return Float.compare(that.getCostFunc(), getCostFunc()) == 0;
    }

    @Override
    public int hashCode() {
        return (getCostFunc() != +0.0f ? Float.floatToIntBits(getCostFunc()) : 0);
    }

    @Override
    public String toString() {
        return "ExpectiMaxNode{" +
                "costFunc=" + costFunc +
                '}';
    }
}
