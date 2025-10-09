
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class ErrorCheck {
    /**
     * checkFileExists checks that the input file you are reading exists
     * 
     * @param inputFile name of input file to read
     * @return boolean value of if file exists
     * @throws IOException to ensure file is found
     */
    public boolean checkFileExists(String inputFile) throws IOException {
        try {
            FileReader reader = new FileReader(inputFile);
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: input file not found: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * checkFileIsNotEmpty checks that the input file you are reading is not empty
     * 
     * @param inputFile name of input file to read
     * @return boolean value of if file is not empty
     */
    public boolean checkFileIsNotEmpty(String inputFile) {
        try {
            File file = new File(inputFile);
            if (file.length() == 0) {
                throw new IOException();
            }
        } catch (IOException e) {
            System.err.println("Error: file is empty: " + e.getMessage());
            return false;
        }
        return true;

    }

    /**
     * checkCSVFileExtension checks that both input and output files has the .csv extension
     * 
     * @param inputFileName  name of input file to check
     * @param outputFileName name of output file to check
     * @return boolean value of if file ends with .csv extension
     */
    public boolean checkCSVFileExtension(String inputFileName, String outputFileName) {
        try {
            if (!(inputFileName.endsWith(".csv")) | !(outputFileName.endsWith(".csv"))) {
                throw new IOException();
            }
        } catch (IOException e) {
            System.err.println("Error: file does not end with .csv: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * checkFilePathExists checks that the file directory you want output file to go to exists and if
     * not error is thrown
     * 
     * @param outputFile name of output file 
     * @return boolen value of if file path exists
     */
    public boolean checkFilePathExists(String outputFile) {
        File file = new File(outputFile);
        File parentDir = file.getParentFile(); // parent directory

        if (parentDir != null && !parentDir.exists()) {
            System.err.println("Error: folder directory does not exist.");
            return false;
        }

        // if file does not exist create it
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    return true;
                }
            } catch (IOException e) {
                System.err.println("Error: Unable to create the file.");
                return false;
            }
        }
        return true;
    }

/**
 * checkRatingIsValid checks that the rating values are all integers between 1 and 5
 * @param inputFile name of input file
 * @return boolean value of if rating is valid
 */

    public boolean checkRatingIsValid(String inputFile) {
        try {
            Reader in = new FileReader(inputFile);
            Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);
            for (CSVRecord data : CSVdata) {
                int rating = Integer.parseInt(data.get(2));
                if (rating > 5 | rating < 0) {
                    throw new Exception();
                }
            }

        } catch (Exception e) {
            System.err.println("Error: invalid integer for song rating: " + e.getMessage());
            return false;
        }
        return true;
    }

/**
 * isFileErrorFree runs all the check methods and returns true boolean value if no errors are thrown
 * @param inputFileName name of input file
 * @param outputFileName name of output file
 * @return boolean value of if files are error free
 * @throws IOException incase error is thrown when checking for errors
 */
    public boolean isFileErrorFree(String inputFileName, String outputFileName) throws IOException {
        if (checkFileExists(inputFileName) &&
                checkFileIsNotEmpty(inputFileName) &&
                checkCSVFileExtension(inputFileName, outputFileName) &&
                checkFilePathExists(outputFileName) &&
                checkRatingIsValid(inputFileName)) {
            return true;
        } else {
            return false;
        }

    }
}