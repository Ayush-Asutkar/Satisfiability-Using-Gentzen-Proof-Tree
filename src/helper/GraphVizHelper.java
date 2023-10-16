package helper;

import java.io.File;
import java.io.IOException;

public class GraphVizHelper {
    public static void createPNGFromDot(String pathToDirectory) throws RuntimeException {
        //compile the dot file
        try {
            compileTheDotFile(pathToDirectory);
        } catch (IOException | InterruptedException e) {
            System.out.println("Could not compile dot file");
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not compile dot file");
        }
        //manually open the png file
    }

    private static void compileTheDotFile(String pathToDirectory) throws IOException, InterruptedException {
        String command = "dot -Tpng graph.dot -o graph.png";
        runOnCommandLine(pathToDirectory, command);
    }

    private static void runOnCommandLine(String pathToDirectory, String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command, null, new File(pathToDirectory));
        int exitCode = process.waitFor();
        System.out.println("Command executed with exit code: " + exitCode);
    }
}
