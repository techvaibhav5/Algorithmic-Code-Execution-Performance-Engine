package com.vaibhav.algoengine.sort;

public class HeapSort implements SortAlgorithm {

    @Override
    public <T extends Comparable<T>> void sort(T[] array) {
        int n = array.length;

        // Build a max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // Repeatedly extract the max and shrink the heap
        for (int i = n - 1; i > 0; i--) {
            swap(array, 0, i);
            heapify(array, i, 0);
        }
    }

    private <T extends Comparable<T>> void heapify(T[] array, int heapSize, int rootIndex) {
        int largest = rootIndex;
        int left = 2 * rootIndex + 1;
        int right = 2 * rootIndex + 2;

        if (left < heapSize && array[left].compareTo(array[largest]) > 0) {
            largest = left;
        }
        if (right < heapSize && array[right].compareTo(array[largest]) > 0) {
            largest = right;
        }

        if (largest != rootIndex) {
            swap(array, rootIndex, largest);
            heapify(array, heapSize, largest);
        }
    }

    private <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
}
