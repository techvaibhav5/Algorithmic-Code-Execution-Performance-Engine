package com.vaibhav.algoengine.sort;

import java.util.Random;

public class QuickSort implements SortAlgorithm {

    private static final Random RANDOM = new Random();

    @Override
    public <T extends Comparable<T>> void sort(T[] array) {
        if (array.length < 2) return;
        quickSort(array, 0, array.length - 1);
    }

    private <T extends Comparable<T>> void quickSort(T[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private <T extends Comparable<T>> int partition(T[] array, int low, int high) {
        // Randomizing the pivot avoids the classic O(n^2) worst case
        // that a fixed-pivot quicksort hits on already-sorted input.
        int randomIndex = low + RANDOM.nextInt(high - low + 1);
        swap(array, randomIndex, high);

        T pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n) avg, O(n^2) worst";
    }
}
