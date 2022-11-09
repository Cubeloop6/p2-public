package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */

public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {

    public class HashTrieNode extends TrieNode<ChainingHashTable<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<>(MoveToFrontList::new);
            this.value = value;
        }

        //No change so far except for changing Map into Chaining Hash


        @Override
        public Iterator<Entry<A, HashTrieNode>> iterator() {

            /**
             * This part was pretty exhausting! phew!!
             */

            Iterator<Item<A, HashTrieNode>> chiHashIt = pointers.iterator();
            //throw new NotYetImplementedException();

            return new Iterator<Entry<A, HashTrieNode>>() {
                @Override
                public boolean hasNext() {
                    return chiHashIt.hasNext();
                }

                @Override
                public Entry<A, HashTrieNode> next() {
                    Item<A, HashTrieNode> retI = chiHashIt.next();
                    return new AbstractMap.SimpleEntry<>(retI.key, retI.value);
                    //throw new NotYetImplementedException();
                }
            }; //end of the inside iterator
        }//end of public iterator method
    } //end of the class


    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
        this.size = 0;
    }

    @Override
    public V insert(K key, V value) {
        //throw new NotYetImplementedException();
        if(key == null || value == null) {
            throw new IllegalArgumentException();
        }
        HashTrieNode node = (HashTrieNode) this.root;
        Iterator<A> it = key.iterator();
        A temp;
        V temp2;

        if(key.isEmpty()){
            temp2 = this.root.value;
            this.root.value = value;
        }
        else {

            while (it.hasNext()) {
                temp = it.next();
                if (node.pointers.find(temp) == null) {
                    node.pointers.insert(temp, new HashTrieNode());
                }
                node = node.pointers.find(temp);
            }

            temp2 = node.value;
            node.value = value;

        }

        if(temp2 == null) {
            this.size++;
        }

        return temp2;
    }

    @Override
    public V find(K key) {
        if(key == null) {
            throw new IllegalArgumentException();
        }
        HashTrieNode node = (HashTrieNode) this.root;
        Iterator<A> it = key.iterator();
        A temp;

        while(it.hasNext()) {
            temp = it.next();
            if(node.pointers.find(temp)== null) {
                return null;
            }
            else{
                node = node.pointers.find(temp);
            }

        }

        return node.value;
        // throw new NotYetImplementedException();
    }

    @Override
    public boolean findPrefix(K key) {
        if(key == null) {
            throw new IllegalArgumentException();
        }
        if(this.root == null) {
            return false;
        }

        HashTrieNode node = (HashTrieNode) this.root;

        for (A temp : key) {
            if (node.pointers.find(temp) != null) {
                node = node.pointers.find(temp);
            }
            else{
                return false;
            }

        }
        return true;
    }

    @Override
    public void delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Iterator<A> it = key.iterator();
        A temp = null;
        HashTrieNode new_node = (HashTrieNode)this.root;

        if (it.hasNext()) {
            temp = it.next();
        }

        for (A low_key : key) {
            if (new_node == null) {
                return;
            }
            if (new_node.pointers.size() == 0) {
                return;
            }
            else {
                new_node = new_node.pointers.find(low_key);
            }
        }

        /**
         *  This part keeps the track of the size
         */

        if (new_node != null && new_node.value != null) {
            if (temp != null) {
                new_node.pointers.delete(temp);
            }
            new_node.value = null;
            this.size--;
        }
    }

    @Override
    public void clear() {
        this.size = 0;
        HashTrieNode node = (HashTrieNode)this.root;
        node.pointers.clear();
    }
}
//push