package com.vaibhav.algoengine.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Adjacency-list graph supporting both directed and undirected edges.
 * LinkedHashMap is used (instead of HashMap) purely so traversal output
 * is deterministic and reproducible when printed for a demo.
 */
public class Graph {

    private final Map<Integer, List<Edge>> adjacencyList = new LinkedHashMap<>();
    private final boolean directed;

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public void addVertex(int vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(int from, int to, int weight) {
        addVertex(from);
        addVertex(to);
        adjacencyList.get(from).add(new Edge(to, weight));
        if (!directed) {
            adjacencyList.get(to).add(new Edge(from, weight));
        }
    }

    public void addEdge(int from, int to) {
        addEdge(from, to, 1);
    }

    public Set<Integer> vertices() {
        return adjacencyList.keySet();
    }

    public List<Edge> neighbors(int vertex) {
        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    }

    public List<Integer> bfs(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            visitOrder.add(current);
            for (Edge edge : neighbors(current)) {
                if (!visited.contains(edge.to)) {
                    visited.add(edge.to);
                    queue.add(edge.to);
                }
            }
        }
        return visitOrder;
    }

    public List<Integer> dfs(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsHelper(start, visited, visitOrder);
        return visitOrder;
    }

    private void dfsHelper(int vertex, Set<Integer> visited, List<Integer> visitOrder) {
        visited.add(vertex);
        visitOrder.add(vertex);
        for (Edge edge : neighbors(vertex)) {
            if (!visited.contains(edge.to)) {
                dfsHelper(edge.to, visited, visitOrder);
            }
        }
    }

    /** Dijkstra's algorithm: shortest distance from start to every reachable vertex. */
    public Map<Integer, Integer> shortestPaths(int start) {
        Map<Integer, Integer> distances = new LinkedHashMap<>();
        for (int v : vertices()) {
            distances.put(v, Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        PriorityQueue<int[]> frontier = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        frontier.add(new int[]{start, 0});
        Set<Integer> finalized = new HashSet<>();

        while (!frontier.isEmpty()) {
            int[] current = frontier.poll();
            int vertex = current[0];
            if (finalized.contains(vertex)) continue;
            finalized.add(vertex);

            for (Edge edge : neighbors(vertex)) {
                int candidateDist = distances.get(vertex) + edge.weight;
                if (candidateDist < distances.get(edge.to)) {
                    distances.put(edge.to, candidateDist);
                    frontier.add(new int[]{edge.to, candidateDist});
                }
            }
        }
        return distances;
    }

    public void printAdjacencyList() {
        for (int vertex : adjacencyList.keySet()) {
            StringBuilder line = new StringBuilder();
            line.append(vertex).append(" -> ");
            for (Edge edge : adjacencyList.get(vertex)) {
                line.append(edge.to).append("(w=").append(edge.weight).append(") ");
            }
            System.out.println(line.toString().trim());
        }
    }
}
