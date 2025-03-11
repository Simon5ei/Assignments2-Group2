package com.example;

public class RedBlackTree<T extends Comparable<T>> extends BalancedBinaryTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class RBNode<T> extends Node<T> {
        boolean color;
        RBNode<T> parent;

        RBNode(T data) {
            super(data);
            this.color = RED;
        }
    }

    @Override
    public void insert(T data) {
        RBNode<T> node = new RBNode<>(data);
        root = insert((RBNode<T>) root, node);
        fixInsert(node);
    }

    private RBNode<T> insert(RBNode<T> root, RBNode<T> node) {
        if (root == null) {
            return node;
        }

        if (node.data.compareTo(root.data) < 0) {
            root.left = insert((RBNode<T>) root.left, node);
            ((RBNode<T>) root.left).parent = root;
        } else if (node.data.compareTo(root.data) > 0) {
            root.right = insert((RBNode<T>) root.right, node);
            ((RBNode<T>) root.right).parent = root;
        }

        return root;
    }

    private void fixInsert(RBNode<T> node) {
        while (node != root && node.parent != null && node.parent.color == RED) {
            if (node.parent == node.parent.parent.left) {
                RBNode<T> uncle = (RBNode<T>) node.parent.parent.right;
                if (uncle != null && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                RBNode<T> uncle = (RBNode<T>) node.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        ((RBNode<T>) root).color = BLACK;
    }

    private void rotateLeft(RBNode<T> x) {
        RBNode<T> y = (RBNode<T>) x.right;
        x.right = y.left;
        if (y.left != null) {
            ((RBNode<T>) y.left).parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(RBNode<T> y) {
        RBNode<T> x = (RBNode<T>) y.left;
        y.left = x.right;
        if (x.right != null) {
            ((RBNode<T>) x.right).parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }

    @Override
    public void delete(T data) {
        RBNode<T> node = (RBNode<T>) search(root, data);
        if (node == null) {
            return;
        }

        RBNode<T> y = node;
        RBNode<T> x;
        boolean yOriginalColor = y.color;

        if (node.left == null) {
            x = (RBNode<T>) node.right;
            transplant(node, (RBNode<T>) node.right);
        } else if (node.right == null) {
            x = (RBNode<T>) node.left;
            transplant(node, (RBNode<T>) node.left);
        } else {
            y = minValueNode((RBNode<T>) node.right);
            yOriginalColor = y.color;
            x = (RBNode<T>) y.right;
            if (y.parent == node) {
                if (x != null) {
                    x.parent = y;
                }
            } else {
                transplant(y, (RBNode<T>) y.right);
                y.right = node.right;
                ((RBNode<T>) y.right).parent = y;
            }
            transplant(node, y);
            y.left = node.left;
            ((RBNode<T>) y.left).parent = y;
            y.color = node.color;
        }

        if (yOriginalColor == BLACK) {
            fixDelete(x);
        }
    }

    private void fixDelete(RBNode<T> x) {
        while (x != root && (x == null || x.color == BLACK)) {
            if (x == null || x.parent == null) {
                break;
            }

            if (x == x.parent.left) {
                RBNode<T> w = (RBNode<T>) x.parent.right;
                if (w == null) {
                    break;
                }

                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = (RBNode<T>) x.parent.right;
                }

                if (w == null || (w.left == null || ((RBNode<T>) w.left).color == BLACK) &&
                    (w.right == null || ((RBNode<T>) w.right).color == BLACK)) {
                    if (w != null) {
                        w.color = RED;
                    }
                    x = x.parent;
                } else {
                    if (w.right == null || ((RBNode<T>) w.right).color == BLACK) {
                        if (w.left != null) {
                            ((RBNode<T>) w.left).color = BLACK;
                        }
                        if (w != null) {
                            w.color = RED;
                        }
                        rotateRight(w);
                        w = (RBNode<T>) x.parent.right;
                    }
                    if (w != null) {
                        w.color = x.parent.color;
                    }
                    x.parent.color = BLACK;
                    if (w != null && w.right != null) {
                        ((RBNode<T>) w.right).color = BLACK;
                    }
                    rotateLeft(x.parent);
                    x = (RBNode<T>) root;
                }
            } else {
                RBNode<T> w = (RBNode<T>) x.parent.left;
                if (w == null) {
                    break;
                }

                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = (RBNode<T>) x.parent.left;
                }

                if (w == null || (w.right == null || ((RBNode<T>) w.right).color == BLACK) &&
                    (w.left == null || ((RBNode<T>) w.left).color == BLACK)) {
                    if (w != null) {
                        w.color = RED;
                    }
                    x = x.parent;
                } else {
                    if (w.left == null || ((RBNode<T>) w.left).color == BLACK) {
                        if (w.right != null) {
                            ((RBNode<T>) w.right).color = BLACK;
                        }
                        if (w != null) {
                            w.color = RED;
                        }
                        rotateLeft(w);
                        w = (RBNode<T>) x.parent.left;
                    }
                    if (w != null) {
                        w.color = x.parent.color;
                    }
                    x.parent.color = BLACK;
                    if (w != null && w.left != null) {
                        ((RBNode<T>) w.left).color = BLACK;
                    }
                    rotateRight(x.parent);
                    x = (RBNode<T>) root;
                }
            }
        }
        if (x != null) {
            x.color = BLACK;
        }
    }

    private void transplant(RBNode<T> u, RBNode<T> v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private RBNode<T> minValueNode(RBNode<T> node) {
        RBNode<T> current = node;
        while (current.left != null) {
            current = (RBNode<T>) current.left;
        }
        return current;
    }

    @Override
    public boolean contains(T data) {
        return search(root, data) != null;
    }

    private Node<T> search(Node<T> node, T data) {
        if (node == null || data.compareTo(node.data) == 0) {
            return node;
        }

        if (data.compareTo(node.data) < 0) {
            return search(node.left, data);
        } else {
            return search(node.right, data);
        }
    }

    @Override
    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public void printTree() {
        printTree((RBNode<T>) root, 0);
    }

    private void printTree(RBNode<T> node, int level) {
        if (node == null) {
            return;
        }

        printTree((RBNode<T>) node.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("   ");
        }
        System.out.println(node.data + " (" + (node.color == RED ? "R" : "B") + ")");
        printTree((RBNode<T>) node.left, level + 1);
    }
}