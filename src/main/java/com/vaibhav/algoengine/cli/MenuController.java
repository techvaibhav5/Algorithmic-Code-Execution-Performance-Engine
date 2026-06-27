package com.vaibhav.algoengine.cli;

import com.vaibhav.algoengine.benchmark.BenchmarkResult;
import com.vaibhav.algoengine.benchmark.SearchBenchmarkRunner;
import com.vaibhav.algoengine.benchmark.SortBenchmarkRunner;
import com.vaibhav.algoengine.graph.Graph;
import com.vaibhav.algoengine.sort.BubbleSort;
import com.vaibhav.algoengine.sort.HeapSort;
import com.vaibhav.algoengine.sort.InsertionSort;
import com.vaibhav.algoengine.sort.MergeSort;
import com.vaibhav.algoengine.sort.QuickSort;
import com.vaibhav.algoengine.sort.SortAlgorithm;
import com.vaibhav.algoengine.tree.AVLTree;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuController {

    private final Scanner scanner = new Scanner(System.in);
    private final List<SortAlgorithm> algorithms = Arrays.asList(
            new BubbleSort(), new InsertionSort(), new MergeSort(), new QuickSort(), new HeapSort()
    );

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    runSortingDemo();
                    break;
                case "2":
                    runTreeDemo();
                    break;
                case "3":
                    runGraphDemo();
                    break;
                case "4":
                    runSearchComplexityBenchmark();
                    break;
                case "5":
                    runConcurrentSortBenchmark();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Not a valid option, try again.\n");
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("=================================================");
        System.out.println(" Algorithmic Code Execution & Performance Engine");
        System.out.println("=================================================");
        System.out.println("1. Sorting algorithm demo + benchmark");
        System.out.println("2. AVL tree demo (insert / search / visualize)");
        System.out.println("3. Graph traversal demo (BFS / DFS / Dijkstra)");
        System.out.println("4. Search complexity benchmark (Linear vs AVL Tree)");
        System.out.println("5. Multithreaded sorting benchmark");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void runSortingDemo() {
        System.out.print("\nEnter array size to benchmark (e.g. 5000): ");
        int size = readInt(5000);

        SortBenchmarkRunner runner = new SortBenchmarkRunner();
        System.out.println("\nRunning each algorithm sequentially on n=" + size + " random integers...\n");
        for (BenchmarkResult result : runner.runSequential(algorithms, size)) {
            System.out.println(result);
        }
        System.out.println();
    }

    private void runTreeDemo() {
        AVLTree<Integer> tree = new AVLTree<>();
        int[] sampleValues = {50, 30, 70, 20, 40, 60, 80, 10, 25};
        for (int v : sampleValues) tree.insert(v);

        System.out.println("\nInserted: " + Arrays.toString(sampleValues));
        System.out.println("Tree height: " + tree.height()
                + " (a plain BST built from sorted input could degrade to height n; AVL keeps it ~log n)");
        System.out.println("In-order traversal: " + tree.inOrder());
        System.out.println("\nTree structure (rotated sideways - right subtree on top, left on bottom):");
        tree.printTree();

        System.out.print("\nEnter a value to search for: ");
        int target = readInt(40);
        boolean found = tree.search(target);
        System.out.println(found
                ? "Found " + target + " in " + tree.getLastSearchComparisons() + " comparisons"
                : target + " not found (checked " + tree.getLastSearchComparisons() + " nodes)");
        System.out.println();
    }

    private void runGraphDemo() {
        Graph graph = new Graph(false);
        graph.addEdge(1, 2, 4);
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 2, 2);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 4, 8);
        graph.addEdge(4, 5, 3);

        System.out.println("\nAdjacency list:");
        graph.printAdjacencyList();

        System.out.println("\nBFS from vertex 1: " + graph.bfs(1));
        System.out.println("DFS from vertex 1: " + graph.dfs(1));

        Map<Integer, Integer> distances = graph.shortestPaths(1);
        System.out.println("\nShortest distances from vertex 1 (Dijkstra):");
        for (Map.Entry<Integer, Integer> entry : distances.entrySet()) {
            System.out.println("  -> " + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println();
    }

    private void runSearchComplexityBenchmark() {
        SearchBenchmarkRunner runner = new SearchBenchmarkRunner();
        int[] sizes = {1_000, 10_000, 100_000, 500_000};

        System.out.println("\nComparing linear search O(n) vs AVL tree search O(log n)");
        System.out.println("(each row averages 200 random lookups, after a warm-up pass)\n");
        System.out.printf("%-10s | %-18s | %-18s | %-18s%n", "n", "Linear avg (ns)", "Tree avg (ns)", "Tree comparisons");
        System.out.println("-".repeat(72));

        for (int size : sizes) {
            SearchBenchmarkRunner.SearchComparison result = runner.compare(size, 200);
            System.out.printf("%-10d | %-18d | %-18d | %-18d%n",
                    result.inputSize, result.linearAvgNanos, result.treeAvgNanos, result.treeAvgComparisons);
        }
        System.out.println();
    }

    private void runConcurrentSortBenchmark() {
        System.out.print("\nEnter array size to benchmark (e.g. 20000 - note Bubble/Insertion Sort are O(n^2), so very large sizes will take a while): ");
        int size = readInt(20_000);

        SortBenchmarkRunner runner = new SortBenchmarkRunner();
        try {
            long wallClockStart = System.nanoTime();
            List<BenchmarkResult> results = runner.runConcurrent(algorithms, size);
            long wallClockElapsed = System.nanoTime() - wallClockStart;

            System.out.println("\nAll " + algorithms.size() + " algorithms ran concurrently, each on its own thread:\n");
            for (BenchmarkResult result : results) {
                System.out.println(result);
            }
            System.out.printf("%nTotal wall-clock time: %.3f ms%n", wallClockElapsed / 1_000_000.0);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Benchmark was interrupted.");
        }
        System.out.println();
    }

    private int readInt(int fallback) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("(using default: " + fallback + ")");
            return fallback;
        }
    }
}
