package com.example;

public abstract class BalancedBinaryTree<T extends Comparable<T>> {
    protected Node<T> root;

    protected static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;

        Node(T data) {
            this.data = data;
        }
    }

    public abstract void insert(T data);
    public abstract void delete(T data);
    public abstract boolean contains(T data);
    public abstract int height();
} 