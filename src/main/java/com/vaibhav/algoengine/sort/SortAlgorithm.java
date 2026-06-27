package com.vaibhav.algoengine.sort;

/**
 * Common contract for every sorting algorithm in this engine.
 * Implementations sort in place and operate on any Comparable type,
 * so the same benchmark harness can drive int arrays, strings, etc.
 */
public interface SortAlgorithm {

    <T extends Comparable<T>> void sort(T[] array);

    /** Human-readable name used in benchmark reports. */
    String getName();

    /** Average-case time complexity, for display purposes only. */
    String getTimeComplexity();
}
