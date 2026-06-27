package com.vaibhav.algoengine.benchmark;

import com.vaibhav.algoengine.tree.AVLTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Empirically backs up the resume claim: a linear scan is O(n), an AVL
 * tree search is O(log n). Each measurement averages many trials with a
 * warm-up pass first, since single nanoTime() calls are too noisy on their
 * own (JIT warm-up, GC pauses, etc. would otherwise dominate the numbers).
 */
public class SearchBenchmarkRunner {

    private static final Random RANDOM = new Random();

    public static class SearchComparison {
        public final int inputSize;
        public final long linearAvgNanos;
        public final long treeAvgNanos;
        public final int treeAvgComparisons;

        public SearchComparison(int inputSize, long linearAvgNanos, long treeAvgNanos, int treeAvgComparisons) {
            this.inputSize = inputSize;
            this.linearAvgNanos = linearAvgNanos;
            this.treeAvgNanos = treeAvgNanos;
            this.treeAvgComparisons = treeAvgComparisons;
        }
    }

    public SearchComparison compare(int inputSize, int trials) {
        List<Integer> values = new ArrayList<>(inputSize);
        for (int i = 0; i < inputSize; i++) {
            values.add(i);
        }
        Collections.shuffle(values, RANDOM);

        AVLTree<Integer> tree = new AVLTree<>();
        for (int v : values) {
            tree.insert(v);
        }

        int[] targets = new int[trials];
        for (int i = 0; i < trials; i++) {
            targets[i] = values.get(RANDOM.nextInt(values.size()));
        }

        // Warm-up pass: let the JIT compile hot methods before we start timing.
        for (int target : targets) {
            linearSearch(values, target);
            tree.search(target);
        }

        long linearTotal = 0;
        for (int target : targets) {
            long start = System.nanoTime();
            linearSearch(values, target);
            linearTotal += System.nanoTime() - start;
        }

        long treeTotal = 0;
        long comparisonsTotal = 0;
        for (int target : targets) {
            long start = System.nanoTime();
            tree.search(target);
            treeTotal += System.nanoTime() - start;
            comparisonsTotal += tree.getLastSearchComparisons();
        }

        return new SearchComparison(
                inputSize,
                linearTotal / trials,
                treeTotal / trials,
                (int) (comparisonsTotal / trials)
        );
    }

    private boolean linearSearch(List<Integer> values, int target) {
        for (int v : values) {
            if (v == target) return true;
        }
        return false;
    }
}
