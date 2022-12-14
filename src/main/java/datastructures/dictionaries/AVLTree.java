package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.worklists.ArrayStack;


/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. Ensure that the class does not have redundant methods
 * 5. Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 * 7. The internal structure of your AVLTree (from this.root to the leaves) must be correct
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {
    // TODO: Implement me!
    private class AVLNode extends BSTNode {
        private int heightDifference;

        public AVLNode(K key, V value) {
            super(key, value);
            this.heightDifference = 0;
        }
    }

    public AVLTree() {
        super();
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        AVLNode current = this.findAVL(key);
        V exVal = current.value;
        current.value = value;
        return exVal;
    }


    private AVLNode rotate(ArrayStack<AVLNode> path) {
        AVLNode grandchild = path.next();
        AVLNode child = path.next();
        AVLNode parent = path.next();
        K first = parent.key;
        K second = child.key;
        K third = grandchild.key;
        int direction = Integer.signum(third.compareTo(first));
        int side = Integer.signum(direction + 1);
        AVLNode remainder = null;
        if (check(first, third, second) || check(second, third, first)) {
            parent.children[side] = grandchild;
            remainder = (AVLNode)grandchild.children[side];
            child.children[1 - side] = remainder;
            grandchild.children[side] = child;
            AVLNode temp = child;
            child = grandchild;
            grandchild = temp;
            child.heightDifference += direction;
            grandchild.heightDifference += direction;
        }
        remainder = (AVLNode)child.children[1 - side];
        parent.children[side] = remainder;
        child.children[1 - side] = parent;
        child.heightDifference += (direction * -1);
        parent.heightDifference += (direction * -2);
        return child;
    }


    private AVLNode findAVL(K key) {
        ArrayStack<AVLNode> path = new ArrayStack<AVLNode>();
        if (this.root == null) {
            this.root = new AVLNode(key, null);
            this.size++;
            return (AVLNode)this.root;
        }
        AVLNode current = (AVLNode)this.root;
        int direction = 0;
        int child = -1;
        AVLNode parentNode = null;

        while (current != null) {
            path.add(current);
            direction = Integer.signum(key.compareTo(current.key));
            if (direction == 0) {
                return current;
            }

            child = Integer.signum(direction + 1);
            current = (AVLNode)current.children[child];
        }

        current = new AVLNode(key, null);
        this.size++;
        AVLNode parent = path.peek();
        path.add(current);
        parent.children[child] = current;
        if (parent.children[1 - child] != null) {
            parent.heightDifference += direction;
        } else {
            parentNode = this.updateHeight(path);
        }
        if (parentNode != null) {
            int subTreeDirection = Integer.signum(key.compareTo(parentNode.key));
            int subTree = Integer.signum(subTreeDirection + 1);
            parentNode.children[subTree] = this.rotate(path);
        }
        return current;
    }

    private AVLNode updateHeight(ArrayStack<AVLNode> path) {
        AVLNode parent = null;
        AVLNode child = null;
        AVLNode grandchild = null;
        while (path.size() > 1) {
            grandchild = child;
            child = path.next();
            parent = path.peek();
            if (child == parent.children[1]) {
                parent.heightDifference++;
            } else {
                parent.heightDifference--;
            }
            if (Math.abs(parent.heightDifference) == 2) {
                if (path.size() == 1) { 
                    this.rotate(path, parent, child, grandchild);
                    this.root = this.rotate(path);
                    return null;
                } else {
                    path.next();
                    AVLNode parentPreRotate = path.peek();
                    this.rotate(path, parent, child, grandchild);
                    return parentPreRotate;
                }
            }
        }
        return null;
    }

    private boolean check(K edge1, K middle, K edge2) {
        return edge1.compareTo(middle) < 0 && middle.compareTo(edge2) < 0;
    }

    private void rotate(ArrayStack<AVLNode> path, AVLNode parent,
                                  AVLNode child, AVLNode grandchild) {

        path.clear();
        path.add(parent);
        path.add(child);
        path.add(grandchild);
    }
}
