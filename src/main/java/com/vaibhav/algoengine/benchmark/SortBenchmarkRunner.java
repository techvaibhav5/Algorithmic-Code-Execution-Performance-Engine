package com.vaibhav.algoengine.benchmark;

import com.vaibhav.algoengine.sort.SortAlgorithm;
import com.vaibhav.algoengine.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SortBenchmarkRunner {

    /** Runs every algorithm against its own random dataset, one at a time, on the calling thread. */
    public List<BenchmarkResult> runSequential(List<SortAlgorithm> algorithms, int inputSize) {
        List<BenchmarkResult> results = new ArrayList<>();
        for (SortAlgorithm algorithm : algorithms) {
            Integer[] data = ArrayUtils.randomArray(inputSize, inputSize * 10);

            long start = System.nanoTime();
            algorithm.sort(data);
            long elapsed = System.nanoTime() - start;

            if (!ArrayUtils.isSorted(data)) {
                throw new IllegalStateException(algorithm.getName() + " produced an incorrectly sorted array");
            }
            results.add(new BenchmarkResult(algorithm.getName(), inputSize, elapsed));
        }
        return results;
    }

    /**
     * Runs every algorithm concurrently, each on its own worker thread, against an
     * identical copy of the same dataset. This is the multithreading piece: a fixed
     * thread pool, one Callable per algorithm, results collected via Future.get().
     */
    public List<BenchmarkResult> runConcurrent(List<SortAlgorithm> algorithms, int inputSize) throws InterruptedException {
        Integer[] sharedData = ArrayUtils.randomArray(inputSize, inputSize * 10);
        int poolSize = Math.min(algorithms.size(), Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(poolSize, 1));
        List<Future<BenchmarkResult>> futures = new ArrayList<>();

        try {
            for (SortAlgorithm algorithm : algorithms) {
                Integer[] dataCopy = ArrayUtils.copyOf(sharedData);
                Callable<BenchmarkResult> task = () -> {
                    long start = System.nanoTime();
                    algorithm.sort(dataCopy);
                    long elapsed = System.nanoTime() - start;

                    if (!ArrayUtils.isSorted(dataCopy)) {
                        throw new IllegalStateException(algorithm.getName() + " produced an incorrectly sorted array");
                    }
                    return new BenchmarkResult(algorithm.getName(), inputSize, elapsed);
                };
                futures.add(executor.submit(task));
            }

            List<BenchmarkResult> results = new ArrayList<>();
            for (Future<BenchmarkResult> future : futures) {
                results.add(future.get());
            }
            return results;
        } catch (ExecutionException e) {
            throw new RuntimeException("A benchmark task failed", e.getCause());
        } finally {
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}
