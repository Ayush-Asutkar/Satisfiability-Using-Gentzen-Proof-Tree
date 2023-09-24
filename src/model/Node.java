package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    private final Set<List<String>> leftHandSide;
    private final Set<List<String>> rightHandSide;
    private Node leftChild;
    private Node rightChild;

    public Node(Set<List<String>> leftHandSide, Set<List<String>> rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
        this.leftChild = null;
        this.rightChild = null;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public List<Node> getNewNodes() {
        return null;
    }

    public boolean isLeafNode() {
        for (List<String> left: this.leftHandSide) {
            if(left.size() > 1) {
                return false;
            }
        }

        for (List<String> right: this.rightHandSide) {
            if(right.size() > 1) {
                return false;
            }
        }

        return true;
    }

    public boolean isContradiction() {
        if (isLeafNode()) {
            for (List<String> left: this.leftHandSide) {
                if(this.rightHandSide.contains(left)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Node{" +
                "leftHandSide=" + leftHandSide +
                ", rightHandSide=" + rightHandSide +
                '}';
    }

    //for testing
    public static void main(String[] args) {
        List<String> leftList1 = new ArrayList<>();
        leftList1.add("Alice");
        List<String> leftList2 = new ArrayList<>();
        leftList2.add("Bob");

        List<String> rightList1 = new ArrayList<>();
        rightList1.add("Bob");

        Set<List<String>> right = new HashSet<>();
        Set<List<String>> left = new HashSet<>();

        left.add(leftList1);
        left.add(leftList2);
        right.add(rightList1);

        Node node = new Node(left, right);
        System.out.println(node);
        System.out.println(node.isLeafNode());
        System.out.println(node.isContradiction());
    }
}
