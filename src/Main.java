import constants.StringOperators;
import helper.GraphVizHelper;
import model.GentzenTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path homeDirectory = FileSystems.getDefault().getPath("").toAbsolutePath();

    private static void deleteOutputDirectory () throws IOException {
        Path path = Path.of(homeDirectory + "\\Output");
//        System.out.println("path: " + path);

        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        assert Files.exists(path);
    }

    private static void createOutputDirectory() throws IOException {
        try {
            deleteOutputDirectory();
        } catch (IOException e) {
//            System.out.println("Output file does not exist yet");
        }
        Path outputDirectoryPath = Path.of(homeDirectory + "\\Output");
        Files.createDirectory(outputDirectoryPath);
    }

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

    private static void printTreeToDotFile(GentzenTree gentzenTree) throws IOException {
        String pathToDotFile = homeDirectory + "\\Output\\Graph.dot";
        gentzenTree.printTreeToGraphvizFile(pathToDotFile);

        String pathToOutputDirectory = homeDirectory + "\\Output";
        GraphVizHelper.createPNGFromDot(pathToOutputDirectory);
    }

    public static void main(String[] args) {
        //create empty output directory
        try {
            createOutputDirectory();
        } catch (IOException e) {
            System.out.println("Could not create output file");
            System.out.println(e.getMessage());
        }

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

        try {
            printTreeToDotFile(gentzenTree);
        } catch (IOException e) {
            System.out.println("Could not write tree to dot file");
            System.out.println(e.getMessage());
        }
    }
}