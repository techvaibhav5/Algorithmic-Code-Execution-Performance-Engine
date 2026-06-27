package com.vaibhav.algoengine.graph;

/** A directed connection to another vertex, with an optional weight (defaults to 1). */
public class Edge {
    public final int to;
    public final int weight;

    public Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}
