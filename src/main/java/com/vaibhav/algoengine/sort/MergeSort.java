package com.vaibhav.algoengine.sort;

public class MergeSort implements SortAlgorithm {

    @Override
    public <T extends Comparable<T>> void sort(T[] array) {
        if (array.length < 2) return;
        mergeSort(array, 0, array.length - 1);
    }

    private <T extends Comparable<T>> void mergeSort(T[] array, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(array, left, mid);
        mergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> void merge(T[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Java won't let us allocate a generic T[] directly, so we stage the
        // halves in plain Object[] buffers and cast back on the way out.
        Object[] leftArr = new Object[n1];
        Object[] rightArr = new Object[n2];
        System.arraycopy(array, left, leftArr, 0, n1);
        System.arraycopy(array, mid + 1, rightArr, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            T l = (T) leftArr[i];
            T r = (T) rightArr[j];
            if (l.compareTo(r) <= 0) {
                array[k++] = l;
                i++;
            } else {
                array[k++] = r;
                j++;
            }
        }
        while (i < n1) array[k++] = (T) leftArr[i++];
        while (j < n2) array[k++] = (T) rightArr[j++];
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
}
