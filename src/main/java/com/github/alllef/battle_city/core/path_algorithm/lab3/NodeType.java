package com.github.alllef.battle_city.core.path_algorithm.lab3;

public enum NodeType {
        MIN, MAX;

        public static NodeType chooseType(NodeType type) {
            if (type == MIN) return MAX;
            if (type == MAX) return MIN;
            return null;
        }
    }