
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class OutputCSV extends InputCalc {
    /**
     * convertToList turns ArrayList of individual (song,number of
     * ratings,mean,standard deviation) data into one ArrayList
     * 
     * @param fileName name of file
     * @return list of all the data from InputCalc
     * @throws IOException in case error is thrown
     */
    public List<List<String>> convertToList(String fileName) throws IOException {

        // individual categories of song data
        ArrayList<String> uniqueSongsList = new ArrayList<>(getUniqueSongs(fileName));
        ArrayList<Integer> songRatingsCountList = new ArrayList<>(getCountOfSong(fileName));
        ArrayList<Double> meanOfRatingList = new ArrayList<>(getMeanOfSongRating(fileName));
        ArrayList<Double> standardDeviationsList = new ArrayList<>(getStandardDeviationOfRating(fileName));

        // list of all data
        ArrayList<List<String>> listOfData = new ArrayList<>();

        // correctly format lists to return columns of data instead of rows
        int numRows = uniqueSongsList.size();
        for (int i = 0; i < numRows; i++) {
            List<String> row = new ArrayList<>();
            row.add(uniqueSongsList.get(i));
            row.add(String.valueOf(songRatingsCountList.get(i))); // String.valueOf to convert int/double to string
            row.add(String.valueOf(meanOfRatingList.get(i)));
            row.add(String.valueOf(standardDeviationsList.get(i)));
            listOfData.add(row);
        }

        return listOfData;
    }

    /**
     * convertToCSV turns ArrayList of all the data into csv
     * 
     * @param inputFileName  name of input file
     * @param outputFileName name of output file
     * @throws IOException in case error is thrown
     */
    public void convertToCSV(String inputFileName, String outputFileName) throws IOException {
        List<List<String>> listOfData = convertToList(inputFileName);

        try (FileWriter writer = new FileWriter(outputFileName);
                @SuppressWarnings("deprecation")
                CSVPrinter csvPrinter = new CSVPrinter(writer,
                        CSVFormat.DEFAULT.withHeader("song", "number of ratings", "mean", "standard deviation"))) {

            // prints data to csv file
            for (List<String> row : listOfData) {
                csvPrinter.printRecord(row);
            }
        }
    }
}