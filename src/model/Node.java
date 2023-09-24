package model;

import constants.Brackets;
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

    public void stripLeftAndRightHandSide() {
        this.stripForSetList(this.leftHandSide);
        this.stripForSetList(this.rightHandSide);
    }

    private void stripForSetList(Set<List<String>> set) {
        for(List<String> formula: set) {
            boolean initialValueBracket = Brackets.isOpeningBracket(formula.get(0));
            while (initialValueBracket) {
                String initialSymbol = formula.get(0);
                String correspondingClosingSymbolForInitialSymbol = Brackets.correspondingClosingBracket(initialSymbol);
                int cnt = 1;


                boolean needToRemove = false;
                for(int i=1; i<formula.size(); i++) {
                    String currSymbol = formula.get(i);
                    if(currSymbol.equals(initialSymbol)) {
                        cnt++;
                    } else if(currSymbol.equals(correspondingClosingSymbolForInitialSymbol)) {
                        cnt--;
                    }

                    if(cnt == 0) {
                        if (i == (formula.size() - 1)) {
                            needToRemove = true;
                        }
                        break;
                    }
                }

                if(needToRemove) {
                    formula.remove(0);
                    formula.remove(formula.size() - 1);
                } else {
                    break;
                }

                initialValueBracket = Brackets.isOpeningBracket(formula.get(0));
            }
        }
    }

    public List<Node> getNewNodes() {
        List<Node> result = new ArrayList<>();

        //loop on left
        for(List<String> left: this.leftHandSide) {

            int indexOfSplitting = this.splittingCharacterIndex(left);

            if(indexOfSplitting < 0) {
                //no splitting allowed
            }
            List<List<String>> brokenFormula = this.breakAFormulaIntoTwoPartsBasedOnIndex(left, indexOfSplitting);

            String symbolOfBreaking = left.get(indexOfSplitting);


            //  ((((((( A U B )))))))
        }

        //loop on right
        return null;
    }

    private List<Node> breakNodeForLeftSide() {
        return null;
    }

    private List<Node> breakForRightSide() {
        return null;
    }

    private List<List<String>> breakAFormulaIntoTwoPartsBasedOnIndex(List<String> formula, int index) {
        List<String> beforeIndex = new ArrayList<>();
        for(int i=0; i<index; i++) {
            beforeIndex.add(formula.get(i));
        }

        List<String> afterIndex = new ArrayList<>();
        for(int i=index+1; i<formula.size(); i++) {
            afterIndex.add(formula.get(i));
        }

        List<List<String>> result = new ArrayList<>();
        result.add(beforeIndex);
        result.add(afterIndex);

        return result;
    }

    /**
     * This function will return the index at which splitting should occur
     * @param formula a list of string which is a particular formula
     * @return index of splitting character, -1 if can not be splitted
     */
    private int splittingCharacterIndex(List<String> formula) {
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
//        list.add("A");
//        list.add(StringOperators.IMPLICATION);
//        list.add("B");
//        list.add(")");
//
//        Set<List<String>> set = new HashSet<>();
//        set.add(new ArrayList<>(list));
//
//        list.clear();
//        list.add("(");
//        list.add("C");
//        list.add(StringOperators.IMPLICATION);
//        list.add("D");
//        list.add(")");
//        set.add(list);
//
//        list.clear();
//        list.add("{");
//        list.add("(");
//        list.add("C");
//        list.add(StringOperators.IMPLICATION);
//        list.add("D");
//        list.add(")");
//        list.add("}");
//        set.add(list);
//
//        list.clear();
//        list.add("{");
//        list.add("(");
//        list.add("C");
//        list.add(StringOperators.IMPLICATION);
//        list.add("D");
//        list.add("}");
//        list.add(")");
//        set.add(list);
//
//
//        System.out.println(set);
//        Node.stripForSetList(set);
//        System.out.println(set);

//
//        for (List<String> formula: set) {
//            System.out.println(formula);
//            formula.remove(0);
//            formula.add("EXIT");
//        }
//        System.out.println(set);

//        List<String> list = new ArrayList<>();
//        list.add("(");
//        list.add("a");
//        list.add(StringOperators.IMPLICATION);
//        list.add("b");
//        list.add(")");
//        list.add(StringOperators.AND);
//        list.add("(");
//        list.add("b");
//        list.add(StringOperators.IMPLICATION);
//        list.add("a");
//        list.add(")");
//
//        System.out.println(list);
//        int result = splittingCharacterIndex(list);
//        System.out.println(result);
//        System.out.println(splittingCharacterIndex(list) + " : " + list.get(splittingCharacterIndex(list)));

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
