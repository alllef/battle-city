package com.github.alllef.battle_city.core.path_algorithm.lab3.minimax_alphabeta;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.util.Direction;

import java.util.List;

public class MiniMaxNode {
    public enum NodeType {
        MIN, MAX;

        static NodeType chooseType(NodeType type) {
            if (type == MIN) return MAX;
            if (type == MAX) return MIN;
            return null;
        }
    }

    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;
    int costFunc = 0;
    boolean traversed = false;
    MiniMaxNode parent;
    List<MiniMaxNode> children;
    Direction dir;
    Rectangle rect;
    NodeType type;

    public MiniMaxNode(int costFunc, MiniMaxNode parent, List<MiniMaxNode> children, Direction dir, Rectangle rect, NodeType type) {
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

    public int getCostFunc() {
        return costFunc;
    }

    public void setCostFunc(int costFunc) {
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

    public boolean isTraversed() {
        return traversed;
    }

    public void setTraversed(boolean traversed) {
        this.traversed = traversed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MiniMaxNode)) return false;

        MiniMaxNode that = (MiniMaxNode) o;

        if (getAlpha() != that.getAlpha()) return false;
        if (getBeta() != that.getBeta()) return false;
        if (getCostFunc() != that.getCostFunc()) return false;
        if (traversed != that.traversed) return false;
        if (getDir() != that.getDir()) return false;
        if (getRect() != null ? !getRect().equals(that.getRect()) : that.getRect() != null) return false;
        return getType() == that.getType();
    }

    @Override
    public int hashCode() {
        int result = getAlpha();
        result = 31 * result + getBeta();
        result = 31 * result + getCostFunc();
        result = 31 * result + (traversed ? 1 : 0);
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
                ", traversed=" + traversed +
                ", parent=" + parent +
                ", children=" + children +
                ", dir=" + dir +
                ", rect=" + rect +
                ", type=" + type +
                '}';
    }
}
