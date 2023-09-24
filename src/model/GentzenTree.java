package model;

import constants.StringOperators;

import java.util.*;

public class GentzenTree {
    private final Node rootNode;
    private final Set<Node> leafNodes;

    public GentzenTree(List<String> expression) {
        Set<List<String>> leftSide = new HashSet<>();
        Set<List<String>> rightSide = new HashSet<>();

        rightSide.add(expression);

        this.rootNode = new Node(leftSide, rightSide);
        this.leafNodes = new HashSet<>();
    }

    public void algorithmForGentzenSystemCreateTree() {
        Queue<Node> unServedNodes = new ArrayDeque<>();
        unServedNodes.add(this.rootNode);

        while(!unServedNodes.isEmpty()) {
            Node currNode = unServedNodes.poll();

            List<Node> newNodesForCurr = currNode.getNewNodes();
            assert newNodesForCurr.size() <= 2;

            switch (newNodesForCurr.size()) {
                case 0: {
                    this.leafNodes.add(currNode);
                    break;
                }

                case 1: {
                    currNode.setLeftChild(newNodesForCurr.get(0));
                    break;
                }

                case 2: {
                    currNode.setLeftChild(newNodesForCurr.get(0));
                    currNode.setRightChild(newNodesForCurr.get(1));
                    break;
                }
            }

            unServedNodes.addAll(newNodesForCurr);
        }
    }

    public void printTree() {
        Queue<Node> q = new ArrayDeque<>();
        q.add(this.rootNode);

        while(!q.isEmpty()) {
            Node curr = q.poll();
            System.out.println(curr);
            System.out.println();

            if(curr.getLeftChild() != null) {
                q.add(curr.getLeftChild());
            }

            if(curr.getRightChild() != null) {
                q.add(curr.getRightChild());
            }
        }
    }

    public boolean checkContradiction() {
        boolean result = false;

        for(Node node: this.leafNodes) {
            if(node.isContradiction()) {
                result = true;
            }
        }

        return result;
    }

    //for testing
    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        list.add("(");
//        list.add(StringOperators.NEGATION);
//        list.add("P");
//        list.add(StringOperators.IMPLICATION);
//        list.add("Q");
//        list.add(")");
//        list.add(StringOperators.IMPLICATION);
//        list.add("(");
//        list.add(StringOperators.NEGATION);
//        list.add("R");
//        list.add(StringOperators.IMPLICATION);
//        list.add("S");
//        list.add(")");


        List<String> list = new ArrayList<>();
        list.add("(");
        list.add("A");
        list.add(StringOperators.AND);
        list.add("(");
        list.add("A");
        list.add(StringOperators.IMPLICATION);
        list.add("B");
        list.add(")");
        list.add(")");
        list.add(StringOperators.IMPLICATION);
        list.add("B");

        GentzenTree gentzenTree = new GentzenTree(list);
        gentzenTree.algorithmForGentzenSystemCreateTree();
        System.out.println(gentzenTree.checkContradiction());

        gentzenTree.printTree();
    }
}
