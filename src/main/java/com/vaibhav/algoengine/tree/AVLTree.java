package com.vaibhav.algoengine.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * A textbook AVL tree: every insert/delete rebalances via single or double
 * rotations so height stays O(log n), which is the whole point of using it
 * over a plain BST that can degrade to a linked list on sorted input.
 */
public class AVLTree<T extends Comparable<T>> {

    private class Node {
        T value;
        Node left, right;
        int height;

        Node(T value) {
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int lastSearchComparisons;

    public void insert(T value) {
        root = insert(root, value);
    }

    private Node insert(Node node, T value) {
        if (node == null) return new Node(value);

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insert(node.left, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, value);
        } else {
            return node; // duplicate values are ignored
        }

        updateHeight(node);
        return rebalance(node);
    }

    public void delete(T value) {
        root = delete(root, value);
    }

    private Node delete(Node node, T value) {
        if (node == null) return null;

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = delete(node.left, value);
        } else if (cmp > 0) {
            node.right = delete(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Two children: replace with the in-order successor (smallest in right subtree)
            Node successor = minNode(node.right);
            node.value = successor.value;
            node.right = delete(node.right, successor.value);
        }

        updateHeight(node);
        return rebalance(node);
    }

    private Node minNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Returns whether the value exists, and records how many node
     * comparisons it took - useful when the benchmark module wants to show
     * "tree search did X comparisons vs linear scan's N".
     */
    public boolean search(T value) {
        lastSearchComparisons = 0;
        return search(root, value);
    }

    private boolean search(Node node, T value) {
        if (node == null) return false;
        lastSearchComparisons++;
        int cmp = value.compareTo(node.value);
        if (cmp == 0) return true;
        return cmp < 0 ? search(node.left, value) : search(node.right, value);
    }

    public int getLastSearchComparisons() {
        return lastSearchComparisons;
    }

    public int height() {
        return height(root);
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rebalance(Node node) {
        int balance = balanceFactor(node);

        if (balance > 1) { // left-heavy
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left); // left-right case
            }
            return rotateRight(node);
        }
        if (balance < -1) { // right-heavy
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right); // right-left case
            }
            return rotateLeft(node);
        }
        return node;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node orphan = x.right;

        x.right = y;
        y.left = orphan;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node orphan = y.left;

        y.left = x;
        x.right = orphan;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    public List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(Node node, List<T> result) {
        if (node == null) return;
        inOrder(node.left, result);
        result.add(node.value);
        inOrder(node.right, result);
    }

    public int size() {
        return inOrder().size();
    }

    /** Prints the tree rotated 90 degrees: right subtree on top, left on bottom. */
    public void printTree() {
        printTree(root, "", true);
    }

    private void printTree(Node node, String prefix, boolean isTail) {
        if (node == null) return;
        printTree(node.right, prefix + (isTail ? "    " : "|   "), false);
        System.out.println(prefix + (isTail ? "+-- " : "+-- ") + node.value);
        printTree(node.left, prefix + (isTail ? "    " : "|   "), true);
    }
}
