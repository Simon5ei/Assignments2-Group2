package com.example;

public class AVLTree<T extends Comparable<T>> extends BalancedBinaryTree<T> {
    private static class AVLNode<T> extends Node<T> {
        int height;

        AVLNode(T data) {
            super(data);
            height = 1;
        }
    }

    @Override
    public void insert(T data) {
        root = insert((AVLNode<T>) root, data);
    }

    private AVLNode<T> insert(AVLNode<T> node, T data) {
        if (node == null) {
            return new AVLNode<>(data);
        }

        if (data.compareTo(node.data) < 0) {
            node.left = insert((AVLNode<T>) node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insert((AVLNode<T>) node.right, data);
        } else {
            return node; // Duplicate data not allowed
        }

        node.height = 1 + Math.max(height((AVLNode<T>) node.left), height((AVLNode<T>) node.right));
        return balance(node);
    }

    private AVLNode<T> balance(AVLNode<T> node) {
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            if (getBalanceFactor((AVLNode<T>) node.left) >= 0) {
                return rotateRight(node);
            } else {
                node.left = rotateLeft((AVLNode<T>) node.left);
                return rotateRight(node);
            }
        }

        if (balanceFactor < -1) {
            if (getBalanceFactor((AVLNode<T>) node.right) <= 0) {
                return rotateLeft(node);
            } else {
                node.right = rotateRight((AVLNode<T>) node.right);
                return rotateLeft(node);
            }
        }

        return node;
    }

    private AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = (AVLNode<T>) y.left;
        AVLNode<T> T2 = (AVLNode<T>) x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height((AVLNode<T>) y.left), height((AVLNode<T>) y.right)) + 1;
        x.height = Math.max(height((AVLNode<T>) x.left), height((AVLNode<T>) x.right)) + 1;

        return x;
    }

    private AVLNode<T> rotateLeft(AVLNode<T> x) {
        AVLNode<T> y = (AVLNode<T>) x.right;
        AVLNode<T> T2 = (AVLNode<T>) y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height((AVLNode<T>) x.left), height((AVLNode<T>) x.right)) + 1;
        y.height = Math.max(height((AVLNode<T>) y.left), height((AVLNode<T>) y.right)) + 1;

        return y;
    }

    private int getBalanceFactor(AVLNode<T> node) {
        if (node == null) {
            return 0;
        }
        return height((AVLNode<T>) node.left) - height((AVLNode<T>) node.right);
    }

    @Override
    public int height() {
        return height((AVLNode<T>) root);
    }

    private int height(AVLNode<T> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    @Override
    public void delete(T data) {
        root = delete((AVLNode<T>) root, data);
    }

    private AVLNode<T> delete(AVLNode<T> node, T data) {
        if (node == null) {
            return null;
        }

        if (data.compareTo(node.data) < 0) {
            node.left = delete((AVLNode<T>) node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = delete((AVLNode<T>) node.right, data);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? (AVLNode<T>) node.right : (AVLNode<T>) node.left;
            } else {
                AVLNode<T> temp = minValueNode((AVLNode<T>) node.right);
                node.data = temp.data;
                node.right = delete((AVLNode<T>) node.right, temp.data);
            }
        }

        if (node == null) {
            return null;
        }

        node.height = 1 + Math.max(height((AVLNode<T>) node.left), height((AVLNode<T>) node.right));
        return balance(node);
    }

    private AVLNode<T> minValueNode(AVLNode<T> node) {
        AVLNode<T> current = node;
        while (current.left != null) {
            current = (AVLNode<T>) current.left;
        }
        return current;
    }

    @Override
    public boolean contains(T data) {
        return contains(root, data);
    }

    private boolean contains(Node<T> node, T data) {
        if (node == null) {
            return false;
        }

        if (data.compareTo(node.data) < 0) {
            return contains(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            return contains(node.right, data);
        } else {
            return true;
        }
    }
} 