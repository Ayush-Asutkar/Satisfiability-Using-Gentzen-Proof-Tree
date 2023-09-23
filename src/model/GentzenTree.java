package model;

import java.util.*;

public class GentzenTree {
    private final Node root;
    private final Set<Node> leafNodes;

    public GentzenTree(String expression) {
        this.root = new Node(expression);
        this.leafNodes = new HashSet<>();
    }

    public void algorithmForGentzenSystemCreateTree() {
        Queue<Node> unServedNodes = new ArrayDeque<>();
        unServedNodes.add(this.root);

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
}
