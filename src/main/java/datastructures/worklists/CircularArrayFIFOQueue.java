package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {

    private int capacity;
    private int front;
    private int back;
    private E array[];
    private int size;

    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        this.array = (E[]) new Comparable[capacity];
        this.capacity = capacity;
        this.front = 0;
        this.back = 0;
        this.size = 0;
    }

    @Override
    public void add(E work) {
        if (isFull()) {
            throw new IllegalStateException();
        }

        array[back] = work;
        back = (back + 1) % capacity;
        size++;

    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }


        return array[front];
    }

    @Override
    public E peek(int i) {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        if (i < 0 || size() <= i) {
            throw new IndexOutOfBoundsException();
        }


        return this.array[(front+i) % capacity];
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        E value = array[front];
        array[front] = null;

        front = (front + 1) % capacity;
        size--;


        return value;
    }

    @Override
    public void update(int i, E value) {
        i = i % capacity;
        array[i] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        array = (E[]) new Object[capacity];

        front = 0;
        back = 0;
        size = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        int minLength = Math.min(this.size(), other.size());
        int compare = 0;
        for (int i = 0; i < minLength; i++) {
            compare = this.peek(i).compareTo(other.peek(i));
            if (compare != 0) {
                return compare;
            }
        }

        return this.size() - other.size();
    }

    @Override
    public boolean equals(Object obj) {
        // You will finish implementing this method in project 2. Leave this method unchanged for project 1.

        if (this == obj) {
            return true;
        }
        else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            if (other.size() != this.size()) {
                return false;
            } else {
                return (this.compareTo(other) == 0);
            }
        }
    }

    @Override
    public int hashCode() {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        int hashCode = 0;
        for(int i = 0; i < this.size; i ++) {
            hashCode += array[(front + i) % array.length].hashCode() * (this.front + i);
        }
        return hashCode * (this.size - this.front);
    }
}
