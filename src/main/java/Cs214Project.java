
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cs214Project {
    public static void main(String[] args) throws Exception {
        if (args.length >= 1 && "-i".equals(args[0])) {
            new CommandlineUI().start();
            return;
        }

        if (args.length < 2) {
            System.err.println("Error: not enough arguments (should be at least 2)");
            throw new IOException();
        }

        String inputFileName  = args[0];
        String outputFileName = args[1];
        String thirdArg       = (args.length >= 3) ? args[2] : null;

        OutputCSV      output        = new OutputCSV();
        UserCalcOutput userOutput    = new UserCalcOutput();
        ErrorCheck     errorCheck     = new ErrorCheck();
        PredictOutput  predictOutput  = new PredictOutput();
        KmeansOutput   kmeansOutput   = new KmeansOutput();

        if (errorCheck.isFileErrorFree(inputFileName, outputFileName)) {
            try {
                if ("-u".equals(thirdArg)) {
                    userOutput.convertToCSV(inputFileName, outputFileName);
                } else if ("-p".equals(thirdArg)) {
                    predictOutput.convertToCSV(inputFileName, outputFileName);
                } else if ("-r".equals(thirdArg)) {
                    if (args.length > 3) {
                        List<String> seeds = new ArrayList<>();
                        for (int i = 3; i < args.length; i++) {
                            seeds.add(args[i]);
                        }
                        kmeansOutput.convertToCSV(inputFileName, outputFileName, seeds);
                    } else {
                        System.err.println("Error: must select at least one song for recommendations");
                    }
                } else if (thirdArg != null) {
                    System.err.println("Error: wrong third argument");
                    throw new IOException();
                } else {
                    output.convertToCSV(inputFileName, outputFileName);
                }
            } catch (IOException e) {
                System.err.println("IOException occurred: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}