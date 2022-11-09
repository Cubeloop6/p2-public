package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;

import java.util.Iterator;


/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {
    private FrontList front;

    private class FrontList {
        private Item<K, V> data;
        private FrontList next;
        public FrontList(Item<K, V> item) {
            this(item, null);
        }

        public FrontList(Item<K, V> item, FrontList next) {
            this.data = item;
            this.next = next;
        }
    }

    public MoveToFrontList() {
        this(null);
    }

    public MoveToFrontList(Item<K, V> item) {
        this.front = new FrontList(item);
        if (item == null) {
            this.size = 0;
        } else {
            this.size = 1;
        }
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        V prev = this.find(key);
        if (prev != null) {
            this.front.data.value = value;
        } else {
            this.front = new FrontList(new Item(key, value), this.front);
            this.size++;
        }
        return prev;
    }

    @Override
    public V find(K key) {

        if (key == null) {
            throw new IllegalArgumentException();
        }
        FrontList current = this.front;
        V returnValue = null;
        if (current != null && current.data != null) {
            if (current.data.key.equals(key)) {
                return current.data.value;
            }
            while (current.next != null && current.next.data != null &&
                    !current.next.data.key.equals(key)) {
                current = current.next;
            }
            if (current.next != null && current.next.data != null) {
                returnValue = current.next.data.value;
                FrontList temp = current.next;
                current.next = temp.next;
                temp.next = this.front;
                this.front = temp;
            }
        }
        return returnValue;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new MoveToFrontListIterator();
    }

    private class MoveToFrontListIterator extends SimpleIterator<Item<K, V>> {
        private FrontList current;

        public MoveToFrontListIterator() {
            this.current = MoveToFrontList.this.front;
        }

        public boolean hasNext() {
            return current != null && current.next != null;
        }

        public Item<K, V> next() {
            Item<K, V> returnItem = current.data;
            current = current.next;
            return returnItem;
        }

    }
}
