
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class UserCalcOutput extends UserCalc {
    /**
     * convertToList turns ArrayList of individual (song,number of
     * ratings,mean,standard deviation) data into one ArrayList
     * 
     * @param fileName name of file
     * @return list of all the data from InputCalc
     * @throws IOException in case error is thrown
     */
    public List<List<String>> convertToList(String fileName) throws Exception {
        Map<String, Map<String, Double>> userSimilarityMap = new HashMap<>(userSimilarityCalc(fileName));

        // individual categories of user data
        ArrayList<String> user1List = new ArrayList<>();
        ArrayList<String> user2List = new ArrayList<>();
        ArrayList<Double> similarityList = new ArrayList<>();

        List<String> users = new ArrayList<>(getUserList(fileName));
        String user1 = "";
        String user2 = "";

        for (int i = 0; i < userSimilarityMap.size(); i++) {
            for (int j = i + 1; j < userSimilarityMap.size(); j++) {
                user1 = users.get(i);
                user2 = users.get(j);
                if (userSimilarityMap.containsKey(user1) && userSimilarityMap.containsKey(user2)) {
                    user1List.add(user1);
                    user2List.add(user2);
                    similarityList.add(userSimilarityMap.get(user1).get(user2));
                }
            }
        }

        // list of all data
        ArrayList<List<String>> listOfData = new ArrayList<>();

        // correctly format lists to return columns of data instead of rows
        int numRows = user1List.size();
        for (int index = 0; index < numRows; index++) {
            List<String> row = new ArrayList<>();
            row.add(user1List.get(index));
            row.add(user2List.get(index));
            row.add(String.valueOf(similarityList.get(index)));
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
    public void convertToCSV(String inputFileName, String outputFileName) throws Exception {
        List<List<String>> listOfData = convertToList(inputFileName);

        try (FileWriter writer = new FileWriter(outputFileName);
                @SuppressWarnings("deprecation")
                CSVPrinter csvPrinter = new CSVPrinter(writer,
                        CSVFormat.DEFAULT.withHeader("name1", "name2", "similarity"))) {

            // prints data to csv file
            for (List<String> row : listOfData) {
                csvPrinter.printRecord(row);
            }
        }
    }
}