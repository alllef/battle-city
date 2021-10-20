package com.github.alllef.battle_city.core.path_algorithm.lab3.expectimax.node;

public abstract class ExpectiMaxNode {
    private float costFunc;
    private boolean traversed = false;

    public float getCostFunc() {
        return costFunc;
    }

    public void setCostFunc(float costFunc) {
        this.costFunc = costFunc;
    }

    public boolean isTraversed() {
        return traversed;
    }

    public void setTraversed(boolean traversed) {
        this.traversed = traversed;
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
                ", traversed=" + traversed +
                '}';
    }
}
