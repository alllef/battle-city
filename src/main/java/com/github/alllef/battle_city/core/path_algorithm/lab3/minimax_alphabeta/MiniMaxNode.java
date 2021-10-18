package com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.util.Direction;

import java.util.List;

public class MiniMaxNode {
    public enum NodeType {MIN, MAX}

    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;
    float costFunc = 0.0f;
    MiniMaxNode parent;
    List<MiniMaxNode> children;
    Direction dir;
    Rectangle rect;
    NodeType type;

    public MiniMaxNode(float costFunc, MiniMaxNode parent, List<MiniMaxNode> children, Direction dir, Rectangle rect, NodeType type) {
        this.costFunc = costFunc;
        this.parent = parent;
        this.children = children;
        this.dir = dir;
        this.rect = rect;
        this.type = type;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public float getCostFunc() {
        return costFunc;
    }

    public void setCostFunc(float costFunc) {
        this.costFunc = costFunc;
    }

    public MiniMaxNode getParent() {
        return parent;
    }

    public void setParent(MiniMaxNode parent) {
        this.parent = parent;
    }

    public List<MiniMaxNode> getChildren() {
        return children;
    }

    public void setChildren(List<MiniMaxNode> children) {
        this.children = children;
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

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MiniMaxNode)) return false;

        MiniMaxNode that = (MiniMaxNode) o;

        if (getAlpha() != that.getAlpha()) return false;
        if (getBeta() != that.getBeta()) return false;
        if (Float.compare(that.getCostFunc(), getCostFunc()) != 0) return false;
        if (getParent() != null ? !getParent().equals(that.getParent()) : that.getParent() != null) return false;
        if (getChildren() != null ? !getChildren().equals(that.getChildren()) : that.getChildren() != null)
            return false;
        if (getDir() != that.getDir()) return false;
        if (getRect() != null ? !getRect().equals(that.getRect()) : that.getRect() != null) return false;
        return getType() == that.getType();
    }

    @Override
    public int hashCode() {
        int result = getAlpha();
        result = 31 * result + getBeta();
        result = 31 * result + (getCostFunc() != +0.0f ? Float.floatToIntBits(getCostFunc()) : 0);
        result = 31 * result + (getParent() != null ? getParent().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        result = 31 * result + (getDir() != null ? getDir().hashCode() : 0);
        result = 31 * result + (getRect() != null ? getRect().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MiniMaxNode{" +
                "alpha=" + alpha +
                ", beta=" + beta +
                ", costFunc=" + costFunc +
                ", parent=" + parent +
                ", children=" + children +
                ", dir=" + dir +
                ", rect=" + rect +
                ", type=" + type +
                '}';
    }
}
