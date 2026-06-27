# Algorithmic Code Execution & Performance Engine

A pure-Java console application that implements core data structures and algorithms from
scratch, then benchmarks them against each other - including a multithreaded benchmark mode
using `ExecutorService`/`Future`.

No frameworks, no external runtime dependencies. Just the JDK.

## What's in it

- **Sorting algorithms**: Bubble Sort, Insertion Sort, Merge Sort, Quick Sort (randomized
  pivot), Heap Sort - all implemented against a shared `SortAlgorithm` interface so the
  benchmark harness can run any of them interchangeably.
- **AVL tree**: a generic, self-balancing binary search tree with insert, delete, search, and an
  ASCII visualizer that prints the tree's actual shape to the console.
- **Graph**: adjacency-list graph supporting BFS, DFS, and Dijkstra's shortest path, weighted or
  unweighted, directed or undirected.
- **Benchmarking**:
  - Sequential sort benchmark - times each algorithm one at a time.
  - **Concurrent sort benchmark** - runs all five algorithms in parallel on a fixed thread pool,
    each with its own `Callable`, collected via `Future.get()`.
  - Search complexity benchmark - empirically compares a linear scan (O(n)) against AVL tree
    search (O(log n)) across increasing input sizes, averaging many trials after a JIT warm-up
    pass so the numbers aren't dominated by cold-start noise.

## Why these choices

- **Randomized pivot in Quick Sort**: a fixed pivot (e.g. always the last element) degrades to
  O(n²) on already-sorted input, which is a common real-world data shape. Randomizing avoids
  that adversarial case.
- **AVL over a plain BST**: a plain BST inserted in sorted order degenerates into a linked list
  (height = n, search = O(n)). AVL rebalances on every insert/delete so height stays O(log n) -
  the tree demo explicitly inserts 1,000 sequential values and shows the height barely moves.
  See `AVLTreeTest.stayBalancedOnSequentialInsertsThatWouldDegradeAPlainBst`.
- **Generic arrays and `Object[]` scratch buffers in Merge Sort**: Java doesn't allow creating a
  generic array (`new T[n]`) directly, so the merge step stages halves in `Object[]` and casts
  back - a real constraint of Java generics, not an oversight.

## Running it

```bash
mvn package
java -jar target/algoengine.jar
```

You'll get an interactive menu:

```
1. Sorting algorithm demo + benchmark
2. AVL tree demo (insert / search / visualize)
3. Graph traversal demo (BFS / DFS / Dijkstra)
4. Search complexity benchmark (Linear vs AVL Tree)
5. Multithreaded sorting benchmark
0. Exit
```

## Running the tests

```bash
mvn test
```

37 tests across sort correctness (random/empty/single-element/duplicate/sorted/reverse-sorted
inputs, parameterized across all five algorithms), AVL tree balance and correctness, and graph
traversal/shortest-path correctness.

## A real benchmark result

From a sample run of the search complexity benchmark (numbers will vary by machine):

```
n          | Linear avg (ns)    | Tree avg (ns)      | Tree comparisons
------------------------------------------------------------------------
1000       | 27317              | 324                | 9
10000      | 6184               | 403                | 12
100000     | 79434              | 670                | 15
500000     | 529946             | 2063               | 18
```

Tree comparisons grow logarithmically (9 -> 12 -> 15 -> 18 as n grows 500x), matching the
expected O(log n) bound, while linear search time scales with n. The dip at n=10,000 versus
n=1,000 is JIT warm-up noise on the very first measurement of the run, not a real performance
inversion - worth knowing if you're asked about it, since it's a common pitfall in
microbenchmarking and a fair thing to be asked about.

## Project structure

```
src/main/java/com/vaibhav/algoengine/
  Main.java
  cli/MenuController.java        - interactive menu
  sort/                          - SortAlgorithm interface + 5 implementations
  tree/AVLTree.java              - generic self-balancing BST
  graph/Graph.java, Edge.java    - adjacency list, BFS/DFS/Dijkstra
  benchmark/                     - sequential + concurrent benchmark runners
  util/ArrayUtils.java           - random array generation, sorted-check, printing
src/test/java/...                - JUnit 5 test suite
```
