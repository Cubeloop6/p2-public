package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;


public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }

    /**
     * Behaviour is undefined when k > array.length
     */
    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        if (k > array.length) {
            k = array.length;
        }
        MinFourHeap<E> sort = new MinFourHeap<E>(comparator);
        for (int i = 0; i < k; i++) {
            sort.add(array[i]);
        }

        for (int j = k; j < array.length; j++) {
            if (comparator.compare(array[j], sort.peek()) > 0) {
                sort.next();
                sort.add(array[j]);
            }
            array[j] = null;

        }

        for (int l = 0; l < k; l++) {
            array[l] = sort.next();
        }

    }
}
