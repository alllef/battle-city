package com.github.alllef.battle_city.core.path_algorithm.lab1;

import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class BFSAlgo extends PathAlgo {
    private record Node(Node parent, Coords child) {
    }

    Queue<Node> coordsQueue = new LinkedList<>();
    boolean[][] climbedPeaksMatrix = new boolean[entityMatr.length][entityMatr.length];
    Coords[][] parentMatrix = new Coords[entityMatr.length][entityMatr.length];

    int num = 0;

    public BFSAlgo(Rectangle startRect, Rectangle endRect) {
        super(startRect, endRect);
    }


    public List<Coords> createAlgo() {
        coordsQueue.add(new Node(null, getFirstVertix()));
        if (coordsQueue.peek().child == null) return new ArrayList<>();

        Node last = coordsQueue.peek();

        while (last != null) {
            nextVertex(last);
            if (isMatrixPart(last.child())) {

                return getPath(last);
            }


            coordsQueue.poll();
            last = coordsQueue.peek();
        }

        return new ArrayList<>();
    }


    public void nextVertex(Node vertex) {
        List<Coords> adjacentVertices = getAdjacentVertices(vertex.child);
        for (Coords adjacentVertix : adjacentVertices) {
            if (!climbedPeaksMatrix[adjacentVertix.x()][adjacentVertix.y()]) {
                num++;
                climbedPeaksMatrix[adjacentVertix.x()][adjacentVertix.y()] = true;
                coordsQueue.add(new Node(vertex, adjacentVertix));
            }
        }
    }

    private List<Coords> getPath(Node lastVertex) {
        List<Coords> coords = new LinkedList<>();
        if (lastVertex.child == null) return coords;

        while (lastVertex.parent != null) {
            coords.add(lastVertex.child());
            lastVertex = lastVertex.parent();
        }
        return coords;
    }

    public int getNum() {
        return num;
    }
}
