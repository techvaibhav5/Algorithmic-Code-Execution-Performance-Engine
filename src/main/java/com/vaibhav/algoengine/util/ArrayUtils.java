package com.vaibhav.algoengine.util;

import java.util.Random;

public class ArrayUtils {

    private static final Random RANDOM = new Random();

    private ArrayUtils() {
    }

    public static Integer[] randomArray(int size, int bound) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RANDOM.nextInt(bound);
        }
        return array;
    }

    public static <T> T[] copyOf(T[] original) {
        return original.clone();
    }

    public static <T extends Comparable<T>> boolean isSorted(T[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1].compareTo(array[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    public static <T> void printArray(T[] array, int maxElements) {
        int limit = Math.min(array.length, maxElements);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < limit; i++) {
            sb.append(array[i]);
            if (i < limit - 1) sb.append(", ");
        }
        if (array.length > maxElements) sb.append(", ...");
        sb.append("]");
        System.out.println(sb);
    }
}
