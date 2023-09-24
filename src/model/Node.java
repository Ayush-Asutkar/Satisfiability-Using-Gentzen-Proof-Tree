package model;

import constants.StringOperators;

import java.util.*;

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

    /**
     * This function will return the index at which splitting should occur
     * @param formula a list of string which is a particular formula
     * @return index of splitting character, -1 if can not be splitted
     */
    private static int splittingCharacterIndex(List<String> formula) {
        //converting to postfix, and return the index of the last operator
        Stack<Integer> stack = new Stack<>();

        int result = -1;

        for (int i=0; i<formula.size(); i++) {
            String symbol = formula.get(i);

            //check if the symbol is an operator
            if(StringOperators.isOperator(symbol)) {
                while (!stack.isEmpty()  &&
                        (StringOperators.precedenceOfOperator(symbol)
                                <= StringOperators.precedenceOfOperator(formula.get(stack.peek())))) {
                    result = stack.peek();
                    stack.pop();
                }
                stack.push(i);
            } else if(symbol.equals("(")) {
                stack.push(i);
            } else if(symbol.equals(")")) {
                while(!stack.isEmpty()  &&  !formula.get(stack.peek()).equals("(")) {
                    result = stack.peek();
                    stack.pop();
                }
                stack.pop();
            }
        }

        while (!stack.isEmpty()) {
            result = stack.pop();
        }

        return result;
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
//        List<String> list = new ArrayList<>();
//        list.add("(");
//        list.add("(");
//        list.add("A");
//        list.add(StringOperators.OR);
//        list.add("P");
//        list.add(")");
//        list.add(StringOperators.AND);
//        list.add("(");
//        list.add("B");
//        list.add(StringOperators.OR);
//        list.add(StringOperators.NEGATION);
//        list.add("P");
//        list.add(")");
//        list.add(")");
//        list.add(StringOperators.IMPLICATION);
//        list.add("(");
//        list.add("A");
//        list.add(StringOperators.OR);
//        list.add("P");
//        list.add(")");
//        list.add(StringOperators.AND);
//        list.add("(");
//        list.add("B");
//        list.add(StringOperators.OR);
//        list.add(StringOperators.NEGATION);
//        list.add("P");
//        list.add(")");

        List<String> list = new ArrayList<>();
        list.add("(");
        list.add("a");
        list.add(StringOperators.IMPLICATION);
        list.add("b");
        list.add(")");
        list.add(StringOperators.AND);
        list.add("(");
        list.add("b");
        list.add(StringOperators.IMPLICATION);
        list.add("a");
        list.add(")");

        System.out.println(list);
        int result = splittingCharacterIndex(list);
        System.out.println(result);
        System.out.println(splittingCharacterIndex(list) + " : " + list.get(splittingCharacterIndex(list)));

//        List<String> leftList1 = new ArrayList<>();
//        leftList1.add("Alice");
//        List<String> leftList2 = new ArrayList<>();
//        leftList2.add("Bob");
//
//        List<String> rightList1 = new ArrayList<>();
//        rightList1.add("Bob");
//
//        Set<List<String>> right = new HashSet<>();
//        Set<List<String>> left = new HashSet<>();
//
//        left.add(leftList1);
//        left.add(leftList2);
//        right.add(rightList1);
//
//        Node node = new Node(left, right);
//        System.out.println(node);
//        System.out.println(node.isLeafNode());
//        System.out.println(node.isContradiction());

//        System.out.println(StringOperators.CONJUNCTION);
//        System.out.println(StringOperators.DISJUNCTION);
//        System.out.println(StringOperators.DOUBLE_IMPLICATION);
//        System.out.println(StringOperators.IMPLICATION);
//        System.out.println(StringOperators.NOT);
    }
}
