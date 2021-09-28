package com.github.alllef.battle_city.core.path_algorithm;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.github.alllef.battle_city.core.util.Coords;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSAlgorithm extends PathAlgo {

    record Node(Node parent, Coords child) {
    }

    Queue<Node> coordsQueue = new LinkedList<>();

    public List<Coords> createAlgo() {
        Sprite tankSprite = playerTank.getSprite();
        Node firstNode = new Node(null, new Coords((int) tankSprite.getX(), (int) tankSprite.getY()));
        coordsQueue.add(firstNode);

        return nextVertex(coordsQueue.poll());

    }



    public List<Coords> nextVertex(Node prevVertex) {
        getAdjacentVertices(prevVertex.child())
                .forEach(coords -> coordsQueue.add(new Node(prevVertex, coords)));

        Node last = coordsQueue.poll();

        if (last == null)
            return null;

        if (isMatrixPart())
            return getPath(last);

        nextVertex(last);
        return null;
    }

    private List<Coords> getPath(Node lastVertex) {
        List<Coords> coords = new LinkedList<>();
        while (lastVertex.parent != null) {
            coords.add(lastVertex.child());
            lastVertex = lastVertex.parent();
        }
        return coords;
    }
}
