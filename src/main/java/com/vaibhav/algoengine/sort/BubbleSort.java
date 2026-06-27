package com.vaibhav.algoengine.sort;

public class BubbleSort implements SortAlgorithm {

    @Override
    public <T extends Comparable<T>> void sort(T[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (array[j].compareTo(array[j + 1]) > 0) {
                    swap(array, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break; // already sorted, no need to keep scanning
        }
    }

    private <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n^2)";
    }
}
