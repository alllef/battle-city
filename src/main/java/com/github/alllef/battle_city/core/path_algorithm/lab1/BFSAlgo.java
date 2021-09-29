package com.github.alllef.battle_city.core.path_algorithm.lab1;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alllef.battle_city.core.path_algorithm.PathAlgo;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.*;

public class BFSAlgo extends PathAlgo {
    private record Node(Node parent, Coords child) {
    }

    Queue<Coords> coordsQueue = new LinkedList<>();
    boolean[][] climbedPeaksMatrix = new boolean[worldSize][worldSize];
    Coords[][] parentMatrix = new Coords[worldSize][worldSize];

    int num = 0;

    public BFSAlgo(Rectangle startRect, Rectangle endRect) {
        super(startRect, endRect);
    }


    public List<Coords> createAlgo() {
        coordsQueue.add(getFirstVertex());
        if (coordsQueue.peek() == null) return new ArrayList<>();

        Coords last = coordsQueue.peek();
        parentMatrix[last.x()][last.y()] = new Coords(-1, -1);
        climbedPeaksMatrix[last.x()][last.y()] = true;

        while (last != null) {
            nextVertex(last);

            if (isMatrixPart(last))
                return getPath(last);

            coordsQueue.poll();
            last = coordsQueue.peek();
        }

        return new ArrayList<>();
    }


    public void nextVertex(Coords vertex) {
        List<Coords> adjacentVertices = getAdjacentVertices(vertex);
        for (Coords adjacentVertex : adjacentVertices) {
            if (!climbedPeaksMatrix[adjacentVertex.x()][adjacentVertex.y()]) {
                num++;
                climbedPeaksMatrix[adjacentVertex.x()][adjacentVertex.y()] = true;
                parentMatrix[adjacentVertex.x()][adjacentVertex.y()] = vertex;
                coordsQueue.add(adjacentVertex);

            }
        }

    }

    private List<Coords> getPath(Coords lastVertex) {
        List<Coords> coords = new LinkedList<>();
        if (lastVertex == null) return coords;

        while (!parentMatrix[lastVertex.x()][lastVertex.y()].equals(new Coords(-1, -1))) {
            coords.add(lastVertex);
            lastVertex = parentMatrix[lastVertex.x()][lastVertex.y()];
        }

        return coords;
    }

    public int getNum() {
        return num;
    }

    public static void main(String[] args) {
        long seconds = TimeUtils.millis();

        Coords[][]matrix = new Coords[1000][1000];
        for (int i=0; i<matrix.length;i++){
            for(int j=0; j<matrix.length;j++){
                matrix[i][j] = new Coords(1,1);
            }
        }

        System.out.println(TimeUtils.millis()-seconds);
    }

}
