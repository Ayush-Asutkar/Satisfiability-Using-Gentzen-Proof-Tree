package model;

import java.util.List;

public class Node {
    private String expression;
    private String lhs;
    private String rhs;
    private Node leftChild;
    private Node rightChild;

    public Node(String expression) {
        this.expression = expression;
        this.leftChild = null;
        this.rightChild = null;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public boolean isLeafNode() {
        return false;
    }

    public boolean isContradiction() {
        if(isLeafNode()) {
            return true;
        }
        return false;
    }

    public List<Node> getNewNodes() {
        return null;
    }

    public String getExpression() {
        return expression;
    }
}
