package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
    private int top;
    private int size;
    private E[] array;

    public ArrayStack() {
        this.top = 0;
        this.size = 0;
        this.array = (E[]) new Object[10];
    }

    @Override
    public void add(E work) {
        if (this.top == this.array.length) {
            int newLength = array.length * 2;
            E[] anotherArray = (E[]) new Object[newLength];
            for (int i = 0; i < this.array.length; i++) {
                anotherArray[i] = array[i];
            }
            array = anotherArray;

        }

        array[this.top] = work;
        this.top++;

        this.size++;


    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        return array[this.top-1];
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
//        E return_value = peek();
        //      array[top] = null;

        this.size--;
        top--;
        return array[this.top];

    }

    @Override
    public int size() {
        return this.top;
    }

    @Override
    public void clear() {
        this.top = 0;
        this.array = (E[])new Object[10];
        this.size=0;
    }
}
