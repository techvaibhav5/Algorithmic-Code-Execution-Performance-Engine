package com.vaibhav.algoengine.benchmark;

public class BenchmarkResult {
    private final String label;
    private final int inputSize;
    private final long elapsedNanos;

    public BenchmarkResult(String label, int inputSize, long elapsedNanos) {
        this.label = label;
        this.inputSize = inputSize;
        this.elapsedNanos = elapsedNanos;
    }

    public String getLabel() {
        return label;
    }

    public int getInputSize() {
        return inputSize;
    }

    public long getElapsedNanos() {
        return elapsedNanos;
    }

    public double getElapsedMillis() {
        return elapsedNanos / 1_000_000.0;
    }

    @Override
    public String toString() {
        return String.format("%-14s | n=%-8d | %10.3f ms", label, inputSize, getElapsedMillis());
    }
}
