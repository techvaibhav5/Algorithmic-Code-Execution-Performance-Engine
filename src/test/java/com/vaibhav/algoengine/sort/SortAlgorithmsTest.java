package com.vaibhav.algoengine.sort;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SortAlgorithmsTest {

    static Stream<SortAlgorithm> algorithms() {
        return Stream.of(new BubbleSort(), new InsertionSort(), new MergeSort(), new QuickSort(), new HeapSort());
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    void sortsRandomArray(SortAlgorithm algorithm) {
        Integer[] input = {5, 3, 8, 1, 9, 2, 7, 4, 6, 0};
        Integer[] expected = input.clone();
        Arrays.sort(expected);

        algorithm.sort(input);

        assertArrayEquals(expected, input, algorithm.getName() + " failed on a random array");
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    void handlesEmptyArray(SortAlgorithm algorithm) {
        Integer[] input = {};
        algorithm.sort(input);
        assertArrayEquals(new Integer[]{}, input);
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    void handlesSingleElement(SortAlgorithm algorithm) {
        Integer[] input = {42};
        algorithm.sort(input);
        assertArrayEquals(new Integer[]{42}, input);
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    void handlesDuplicates(SortAlgorithm algorithm) {
        Integer[] input = {4, 2, 4, 1, 2, 4, 1};
        Integer[] expected = input.clone();
        Arrays.sort(expected);

        algorithm.sort(input);

        assertArrayEquals(expected, input, algorithm.getName() + " failed with duplicate values");
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    void handlesAlreadySortedInput(SortAlgorithm algorithm) {
        Integer[] input = {1, 2, 3, 4, 5, 6, 7, 8};
        Integer[] expected = input.clone();

        algorithm.sort(input);

        assertArrayEquals(expected, input, algorithm.getName() + " failed on already-sorted input");
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    void handlesReverseSortedInput(SortAlgorithm algorithm) {
        Integer[] input = {8, 7, 6, 5, 4, 3, 2, 1};
        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8};

        algorithm.sort(input);

        assertArrayEquals(expected, input, algorithm.getName() + " failed on reverse-sorted input");
    }
}
