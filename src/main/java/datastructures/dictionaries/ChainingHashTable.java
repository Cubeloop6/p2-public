package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;


/**
 * 1. You must implement a generic chaining hashtable. You may not
 * restrict the size of the input domain (i.e., it must accept
 * any key) or the number of inputs (i.e., it must grow as necessary).
 * 3. Your HashTable should rehash as appropriate (use load factor as
 * shown in class!).
 * 5. HashTable should be able to resize its capacity to prime numbers for more
 * than 200,000 elements. After more than 200,000 elements, it should
 * continue to resize using some other mechanism.
 * 6. We suggest you hard code some prime numbers. You can use this
 * list: http://primes.utm.edu/lists/small/100000.txt
 * NOTE: Do NOT copy the whole list!
 * 7. When implementing your iterator, you should NOT copy every item to another
 * dictionary/list and return that dictionary/list's iterator.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>>chain;
    private final int[] sizes = {17, 37, 79, 164, 331, 673, 1361, 2729, 5471, 10949, 21911, 43853, 87719, 175447, 350899, 701819};
    private int starting;
    private double count;
    private double loadFactor;
    private Dictionary<K,V>[] array;
    private int counter;

    public ChainingHashTable(Supplier<Dictionary<K, V>>chain) {
        this.chain =chain;
        loadFactor = 0.0;
        array = new Dictionary[7];
        for(int i = 0; i < 7; i++) {
            array[i] =chain.get();
        }
        starting = 0;
        count = 0;
        counter = 0;
    }

    public int size() {
        return counter;
    }

    @Override
    public V insert(K key, V value) {
        if(loadFactor >= 1) {
            this.array = resize(array);
        }
        int index = Math.abs(key.hashCode() % array.length);
        if(index >= 0) {
            if(array[index] == null) {
                array[index] =chain.get();
            }
            V val = null;
            if(this.find(key) == null) {
                counter++;
            } else {
                val = this.find(key);
            }
            array[index].insert(key, value);
            loadFactor = (++count) / array.length;
            return val;
        } else {
            return null;
        }
    }

    @Override
    public V find(K key) {
        int index = Math.abs(key.hashCode() % array.length);
        if(index >= 0) {
            if(array[index] == null) {
                array[index] =chain.get();
                return null;
            }
            return array[index].find(key);
        } else {
            return null;
        }
    }

    @Override
    public Iterator<Item<K, V>> iterator() {

        if(array[0] == null) {
            array[0] =chain.get();
        }
        Iterator<Item<K,V>> it = new Iterator<Item<K,V>>() {
            private int iterator = 0;

            Iterator<Item<K,V>> iterator2 = array[0].iterator();

            @Override
            public boolean hasNext() {
                if(iterator < array.length && !iterator2.hasNext()) {
                    if(array[iterator + 1] == null) {
                        iterator++;

                        while(array[iterator] == null) {
                            iterator++;
                            if(iterator >= array.length) {
                                return false;
                            }
                        }
                    } else {
                        iterator++;
                    }
                    if(iterator < array.length) {
                        iterator2 = array[iterator].iterator();
                    }
                }
                if(iterator >= array.length) {
                    return false;
                } else {
                    return iterator2.hasNext();
                }
            }

            @Override
            public Item<K, V> next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return iterator2.next();
            }
        };
        return it;
    }

    private Dictionary<K,V>[] resize(Dictionary<K,V> arrayChange[]) {
        Dictionary<K,V>[] dict;
        if(starting > 15) {
            dict = new Dictionary[arrayChange.length * 2];
        } else {
            dict = new Dictionary[sizes[starting]];
        }
        for(int i = 0; i < arrayChange.length; i++) {
            if(arrayChange[i] != null) {
                for(Item<K,V> item : arrayChange[i]) {
                    int index = Math.abs(item.key.hashCode() % dict.length);
                    if(index >= 0) {
                        if(dict[index] == null) {
                            dict[index] = chain.get();
                        }
                        dict[index].insert(item.key, item.value);
                    } else {
                        return new Dictionary[0];
                    }
                }
            }
        }
        starting++;
        return dict;

    }

    /**
     * Temporary fix so that you can debug on IntelliJ properly despite a broken iterator
     * Remove to see proper String representation (inherited from Dictionary)
     */
    @Override
    public String toString() {
        return "ChainingHashTable String representation goes here.";
    }
}
