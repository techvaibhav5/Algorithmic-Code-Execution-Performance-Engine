package com.vaibhav.algoengine.tree;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AVLTreeTest {

    @Test
    void insertAndSearchFindExistingValues() {
        AVLTree<Integer> tree = new AVLTree<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) tree.insert(v);

        for (int v : values) {
            assertTrue(tree.search(v), "Expected to find " + v);
        }
        assertFalse(tree.search(999));
    }

    @Test
    void inOrderTraversalIsSorted() {
        AVLTree<Integer> tree = new AVLTree<>();
        int[] values = {9, 4, 17, 2, 6, 23, 1, 5};
        for (int v : values) tree.insert(v);

        List<Integer> sorted = tree.inOrder();
        for (int i = 1; i < sorted.size(); i++) {
            assertTrue(sorted.get(i - 1) <= sorted.get(i));
        }
        assertEquals(values.length, sorted.size());
    }

    @Test
    void stayBalancedOnSequentialInsertsThatWouldDegradeAPlainBst() {
        AVLTree<Integer> tree = new AVLTree<>();
        // Inserting in sorted order is the classic case that turns an unbalanced
        // BST into a linked list (height = n). An AVL tree must not do that.
        for (int i = 1; i <= 1000; i++) {
            tree.insert(i);
        }

        int height = tree.height();
        // log2(1000) ~= 10; AVL guarantees height < 1.45 * log2(n + 2)
        assertTrue(height < 20, "AVL tree height grew too large: " + height);
    }

    @Test
    void deleteRemovesValueAndKeepsTreeValid() {
        AVLTree<Integer> tree = new AVLTree<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) tree.insert(v);

        tree.delete(30);

        assertFalse(tree.search(30));
        for (int v : values) {
            if (v != 30) {
                assertTrue(tree.search(v));
            }
        }
        assertEquals(values.length - 1, tree.size());
    }
}
