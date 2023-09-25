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
        this.leftHandSide = new HashSet<>();
        for(List<String> left: leftHandSide) {
            if(left == null  ||  left.isEmpty()) {
                continue;
            } else {
                this.leftHandSide.add(left);
            }
        }

        this.rightHandSide = new HashSet<>();
        for(List<String> right: rightHandSide) {
            if(right == null  ||  right.isEmpty()) {
                continue;
            } else {
                this.rightHandSide.add(right);
            }
        }

        this.leftChild = null;
        this.rightChild = null;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node getLeftChild() {
        return this.leftChild;
    }

    public Node getRightChild() {
        return this.rightChild;
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
        //loop on left
        for(List<String> left: this.leftHandSide) {
            int indexOfSplitting = this.splittingCharacterIndex(left);

            if(indexOfSplitting < 0) {
                //no splitting allowed
                continue;
            }

            List<Node> result = breakNodeForLeftSide(left, indexOfSplitting);

            assert result != null;

            for(Node node: result) {
                node.stripLeftAndRightHandSide();
            }
            return result;
        }

        //loop on right
        for(List<String> right: this.rightHandSide) {
            int indexOfSplitting = this.splittingCharacterIndex(right);

            if(indexOfSplitting < 0) {
                //no splitting allowed
                continue;
            }

            List<Node> result = breakForRightSide(right, indexOfSplitting);

            assert result != null;

            for(Node node: result) {
                node.stripLeftAndRightHandSide();
            }
            return result;
        }

        //return empty list
        return new ArrayList<>();
    }

    private static Set<List<String>> deepCopyASetOfListOfString(Set<List<String>> input) {
        Set<List<String>> result = new HashSet<>();
        for(List<String> formula: input) {
            result.add(new ArrayList<>(formula));
        }
        return result;
    }

    private List<String> generateNewFormulaForDoubleImplication(List<String> first, List<String> second) {
        List<String> firstImplicationFormula = new ArrayList<>();

        firstImplicationFormula.addAll(new ArrayList<>(first));
        firstImplicationFormula.add(StringOperators.IMPLICATION);
        firstImplicationFormula.addAll(new ArrayList<>(second));

        firstImplicationFormula = Brackets.appendBracketAtStartAndEnd(firstImplicationFormula);

        List<String> secondImplicationFormula = new ArrayList<>();

        secondImplicationFormula.addAll(new ArrayList<>(second));
        secondImplicationFormula.add(StringOperators.IMPLICATION);
        secondImplicationFormula.addAll(new ArrayList<>(first));

        secondImplicationFormula = Brackets.appendBracketAtStartAndEnd(secondImplicationFormula);

        List<String> result = new ArrayList<>();
        result.addAll(firstImplicationFormula);
        result.add(StringOperators.AND);
        result.addAll(secondImplicationFormula);

        result = Brackets.appendBracketAtStartAndEnd(result);

        return result;
    }

    private List<Node> breakNodeForLeftSide(List<String> left, int index) {
        List<List<String>> brokenFormula = this.breakAFormulaIntoTwoPartsBasedOnIndex(left, index);
        String symbolOnWhichBreaking = left.get(index);

        if(symbolOnWhichBreaking.equals(StringOperators.DOUBLE_IMPLICATION)) {
            //create one different node
            //                 Lambda, A <-> B => delta
            //   i) Lambda, (A -> B)  and (B -> A) => delta

            //Only one new Node
            Set<List<String>> newLeftSide = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSide.remove(left);

            List<String> newFormula = this.generateNewFormulaForDoubleImplication(brokenFormula.get(0), brokenFormula.get(1));

            newLeftSide.add(newFormula);

            Set<List<String>> newRightSide = deepCopyASetOfListOfString(this.rightHandSide);

            Node newNode = new Node(newLeftSide, newRightSide);

            List<Node> listNode = new ArrayList<>();
            listNode.add(newNode);

            return listNode;
        } else if(symbolOnWhichBreaking.equals(StringOperators.IMPLICATION)) {
            //create two different nodes
            //      Lambda, A->B => delta
            //     i)  Lambda, B => delta
            //    ii)     Lambda => A, delta

            //For first
            Set<List<String>> newLeftSideFirst = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSideFirst.remove(left);
            newLeftSideFirst.add(brokenFormula.get(1));

            Set<List<String>> newRightSideFirst = deepCopyASetOfListOfString(this.rightHandSide);
            Node firstNode = new Node(newLeftSideFirst, newRightSideFirst);

            //For second
            Set<List<String>> newLeftSideSecond = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSideSecond.remove(left);

            Set<List<String>> newRightSecond = deepCopyASetOfListOfString(this.rightHandSide);
            newRightSecond.add(brokenFormula.get(0));
            Node secondNode = new Node(newLeftSideSecond, newRightSecond);

            List<Node> nodeList = new ArrayList<>();
            nodeList.add(firstNode);
            nodeList.add(secondNode);

            return nodeList;
        } else if(symbolOnWhichBreaking.equals(StringOperators.AND)) {
            //create one different nodes
            //      Lambda, A and B => delta
            //       Lambda, A, B   => delta

            //Only one new Node
            Set<List<String>> newLeftSide = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSide.remove(left);
            newLeftSide.add(brokenFormula.get(0));
            newLeftSide.add(brokenFormula.get(1));

            Set<List<String>> newRightSide = deepCopyASetOfListOfString(this.rightHandSide);

            Node newNode = new Node(newLeftSide, newRightSide);

            List<Node> listNode = new ArrayList<>();
            listNode.add(newNode);

            return listNode;
        } else if(symbolOnWhichBreaking.equals(StringOperators.OR)) {
            //create two different nodes
            //      Lambda, A or B => delta
            //     i)    Lambda, A => delta
            //    ii)    Lambda, B => delta

            //For first
            Set<List<String>> newLeftSideFirst = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSideFirst.remove(left);
            newLeftSideFirst.add(brokenFormula.get(0));

            Set<List<String>> newRightSideFirst = deepCopyASetOfListOfString(this.rightHandSide);
            Node firstNode = new Node(newLeftSideFirst, newRightSideFirst);

            //For second
            Set<List<String>> newLeftSideSecond = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSideSecond.remove(left);
            newLeftSideSecond.add(brokenFormula.get(1));

            Set<List<String>> newRightSecond = deepCopyASetOfListOfString(this.rightHandSide);
            Node secondNode = new Node(newLeftSideSecond, newRightSecond);

            List<Node> nodeList = new ArrayList<>();
            nodeList.add(firstNode);
            nodeList.add(secondNode);

            return nodeList;
        } else if(symbolOnWhichBreaking.equals(StringOperators.NEGATION)) {
            //create one different nodes
            //      Lambda, not A => delta
            //            Lambda  => A, delta

            assert brokenFormula.get(0).isEmpty();

            //Only one new Node
            Set<List<String>> newLeftSide = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSide.remove(left);

            Set<List<String>> newRightSide = deepCopyASetOfListOfString(this.rightHandSide);
            newRightSide.add(brokenFormula.get(1));

            Node newNode = new Node(newLeftSide, newRightSide);

            List<Node> listNode = new ArrayList<>();
            listNode.add(newNode);

            return listNode;
        } else {
            return null;
        }
    }

    private List<Node> breakForRightSide(List<String> right, int index) {
        List<List<String>> brokenFormula = this.breakAFormulaIntoTwoPartsBasedOnIndex(right, index);
        String symbolOnWhichBreaking = right.get(index);

        if(symbolOnWhichBreaking.equals(StringOperators.DOUBLE_IMPLICATION)) {
            //create one different node
            //      Lambda => A <-> B, delta
            //   i) Lambda => (A -> B)  and (B -> A), delta

            //Only one new Node
            Set<List<String>> newLeftSide = deepCopyASetOfListOfString(this.leftHandSide);

            Set<List<String>> newRightSide = deepCopyASetOfListOfString(this.rightHandSide);
            newRightSide.remove(right);

            List<String> newFormula = this.generateNewFormulaForDoubleImplication(brokenFormula.get(0), brokenFormula.get(1));

            newRightSide.add(newFormula);

            Node newNode = new Node(newLeftSide, newRightSide);

            List<Node> listNode = new ArrayList<>();
            listNode.add(newNode);

            return listNode;
        } else if(symbolOnWhichBreaking.equals(StringOperators.IMPLICATION)) {
            //create one different nodes
            //      Lambda =>  A->B, delta
            //   Lambda, A => B, delta

            //Only one new Node
            Set<List<String>> newLeftSide = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSide.add(brokenFormula.get(0));

            Set<List<String>> newRightSide = deepCopyASetOfListOfString(this.rightHandSide);
            newRightSide.remove(right);
            newRightSide.add(brokenFormula.get(1));

            Node newNode = new Node(newLeftSide, newRightSide);

            List<Node> listNode = new ArrayList<>();
            listNode.add(newNode);

            return listNode;
        } else if(symbolOnWhichBreaking.equals(StringOperators.AND)) {
            //create two different nodes
            //        Lambda =>  A and B, delta
            //     i) Lambda => A, delta
            //    ii) Lambda => B, delta

            //For first
            Set<List<String>> newLeftSideFirst = deepCopyASetOfListOfString(this.leftHandSide);

            Set<List<String>> newRightSideFirst = deepCopyASetOfListOfString(this.rightHandSide);
            newRightSideFirst.remove(right);
            newRightSideFirst.add(brokenFormula.get(0));

            Node firstNode = new Node(newLeftSideFirst, newRightSideFirst);

            //For second
            Set<List<String>> newLeftSideSecond = deepCopyASetOfListOfString(this.leftHandSide);

            Set<List<String>> newRightSecond = deepCopyASetOfListOfString(this.rightHandSide);
            newRightSecond.remove(right);
            newRightSecond.add(brokenFormula.get(1));

            Node secondNode = new Node(newLeftSideSecond, newRightSecond);

            List<Node> nodeList = new ArrayList<>();
            nodeList.add(firstNode);
            nodeList.add(secondNode);

            return nodeList;
        } else if(symbolOnWhichBreaking.equals(StringOperators.OR)) {
            //create one different nodes
            //      Lambda  => A or B, delta
            //      Lambda  => A, B, delta

            //Only one new Node
            Set<List<String>> newLeftSide = deepCopyASetOfListOfString(this.leftHandSide);

            Set<List<String>> newRightSide = deepCopyASetOfListOfString(this.rightHandSide);
            newRightSide.remove(right);
            newRightSide.add(brokenFormula.get(0));
            newRightSide.add(brokenFormula.get(1));

            Node newNode = new Node(newLeftSide, newRightSide);

            List<Node> listNode = new ArrayList<>();
            listNode.add(newNode);

            return listNode;
        } else if(symbolOnWhichBreaking.equals(StringOperators.NEGATION)) {
            //create two different nodes
            //      Lambda => not A, delta
            //  Lambda, A  => delta

            assert brokenFormula.get(0).isEmpty();

            //Only one new Node
            Set<List<String>> newLeftSide = deepCopyASetOfListOfString(this.leftHandSide);
            newLeftSide.add(brokenFormula.get(1));

            Set<List<String>> newRightSide = deepCopyASetOfListOfString(this.rightHandSide);
            newRightSide.remove(right);

            Node newNode = new Node(newLeftSide, newRightSide);

            List<Node> listNode = new ArrayList<>();
            listNode.add(newNode);

            return listNode;
        } else {
            return null;
        }
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

        if(!stack.isEmpty()) {
            result = stack.peek();
        }
        while (!stack.isEmpty()) {
            stack.pop();
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
        return leftHandSide + " \u21D2 " + rightHandSide;
    }

    //for testing
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("(");
        list.add(StringOperators.NEGATION);
        list.add("A");
        list.add(StringOperators.DOUBLE_IMPLICATION);
        list.add("B");
        list.add(")");

        Set<List<String>> leftSide = new HashSet<>();
        leftSide.add(new ArrayList<>(list));

        list.clear();
        list.add("B");
//        list.add("(");
//        list.add("C");
//        list.add(StringOperators.OR);
//        list.add("D");
//        list.add(")");

        Set<List<String>> rightSide = new HashSet<>();
        rightSide.add(new ArrayList<>(list));

        Node node = new Node(leftSide, rightSide);
        node.stripLeftAndRightHandSide();
        System.out.println(node);
//        System.out.println(node.isContradiction());

        List<Node> newNodes = node.getNewNodes();
        for(Node n: newNodes) {
            System.out.println(n);
            System.out.println(n.isContradiction());
        }
//
//        List<Node> newNodes2 = newNodes.get(1).getNewNodes();
//        System.out.println(newNodes2);
//        for(Node n: newNodes2) {
//            System.out.println(n);
//            System.out.println(n.isContradiction());
//        }



//        List<String> list = new ArrayList<>();
//        list.add("(");
//        list.add("A");
//        list.add(StringOperators.IMPLICATION);
//        list.add("(");
//        list.add("B");
//        list.add(StringOperators.OR);
//        list.add("C");
//        list.add(")");
//        list.add(")");
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
//        set.add(new ArrayList<>(list));
//
//        list.clear();
//        list.add("{");
//        list.add("(");
//        list.add("C");
//        list.add(StringOperators.IMPLICATION);
//        list.add("D");
//        list.add(")");
//        list.add("}");
//        set.add(new ArrayList<>(list));
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
