package com.vaibhav.algoengine.graph;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphTest {

    @Test
    void bfsVisitsAllReachableVertices() {
        Graph graph = new Graph(false);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);

        List<Integer> order = graph.bfs(1);
        assertEquals(4, order.size());
        assertTrue(order.contains(1));
        assertTrue(order.contains(2));
        assertTrue(order.contains(3));
        assertTrue(order.contains(4));
        assertEquals(1, order.get(0)); // BFS always starts at the source
    }

    @Test
    void dfsVisitsAllReachableVertices() {
        Graph graph = new Graph(false);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        List<Integer> order = graph.dfs(1);
        assertEquals(4, order.size());
        assertEquals(1, order.get(0));
    }

    @Test
    void dijkstraFindsShortestDistances() {
        Graph graph = new Graph(false);
        graph.addEdge(1, 2, 4);
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 2, 2);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 4, 8);

        Map<Integer, Integer> distances = graph.shortestPaths(1);

        assertEquals(0, distances.get(1));
        assertEquals(3, distances.get(2)); // 1->3 (1) + 3->2 (2) = 3, cheaper than the direct edge of weight 4
        assertEquals(1, distances.get(3));
        assertEquals(8, distances.get(4)); // 1->3->2->4 = 1 + 2 + 5 = 8
    }
}
