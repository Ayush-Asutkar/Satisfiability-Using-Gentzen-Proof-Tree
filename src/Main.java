import constants.StringOperators;
import model.GentzenTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<String> takeInputRead() {
        System.out.print("Write space separated input: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        sc.close();
        String[] inputSplit = input.split(" ");
        List<String> list = new ArrayList<>();
        for(String in: inputSplit) {
            if(in != null  &&  !in.isEmpty()) {
                list.add(in);
            }
        }

        return list;
    }

    private static void printAvailableOperators() {
        System.out.println("Available operators are:- ");
        System.out.println("\tOr: " + StringOperators.OR);
        System.out.println("\tAnd: " + StringOperators.AND);
        System.out.println("\tDouble Implication: " + StringOperators.DOUBLE_IMPLICATION);
        System.out.println("\tImplication: " + StringOperators.IMPLICATION);
        System.out.println("\tNegation: " + StringOperators.NEGATION);

        System.out.println("\nNOTE: Please use the above operators only, all other symbols will be considered as symbols!\n");
    }

    public static void main(String[] args) {
        printAvailableOperators();

        List<String> input = takeInputRead();
//        System.out.println(input);

        GentzenTree gentzenTree = new GentzenTree(input);

        System.out.println("Applying Gentzen Tree Proof");
        gentzenTree.applyAlgorithmForGentzenSystemCreateTree();

        gentzenTree.printTree();

        System.out.println("Contradiction at any leaf node: " + gentzenTree.checkContradiction());
        if(gentzenTree.checkContradiction()) {
            gentzenTree.printNodeWhichIsContradicting();
            System.out.println("The given expression is satisfiable");
        } else {
            System.out.println("The given expression is not satisfiable");
        }
    }
}