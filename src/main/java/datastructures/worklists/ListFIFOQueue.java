package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;


/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

    Node beginning;
    Node end;
    int size;
    private class Node {
        E data;
        Node next;
        Node(E work) {
            data = work;
            next = null;

        }
    }

    public ListFIFOQueue() {

    }

    @Override
    public void add(E work) {
        if (!hasWork()) {
            beginning = end = new Node(work);
        }
        else {
            end.next = new Node(work);
            end = end.next;
        }
        size++;
    }


    @Override
    public E peek() {
        if(!hasWork()) {
            throw new NoSuchElementException();
        }
        return (E) beginning.data;
    }

    @Override
    public E next() {
        if(!hasWork()) {
            throw new NoSuchElementException();
        }
        size--;
        Node return_data = beginning;
        Node temp = beginning;
        beginning = beginning.next;
        temp = null;
        return return_data.data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        beginning.next = beginning;
        beginning = null;
        size = 0;
    }

    private class ListNode {

        public E data;
        public ListNode next;

        public ListNode(E data) {
            this.data = data;
            next = null;
        }

    }

}
