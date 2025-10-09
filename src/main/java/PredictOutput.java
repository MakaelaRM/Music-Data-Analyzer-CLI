
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class PredictOutput extends Predict {

    public List<List<String>> convertToList(String fileName) throws Exception {
        Map<String, Map<String, Double>> predictionMap = predictMissingRatings(fileName);
        String ratingString = "";

        TreeMap<String, TreeMap<String, String>> sortedData = new TreeMap<>();

        for (String song : predictionMap.keySet()) {
            Map<String, Double> userRatings = predictionMap.get(song);
            for (String user : userRatings.keySet()) {
                Double rating = userRatings.get(user);

                if (Double.isNaN(rating)) {
                    ratingString = "NaN";
                } else {
                    ratingString = Integer.toString(rating.intValue());
                }

                sortedData.computeIfAbsent(song, k -> new TreeMap<>()).put(user, ratingString);
            }
        }

        ArrayList<List<String>> listOfData = new ArrayList<>();
        for (String song : sortedData.keySet()) {
            TreeMap<String, String> userMap = sortedData.get(song);
            for (String user : userMap.keySet()) {
                List<String> row = new ArrayList<>();
                row.add(song);
                row.add(user);
                row.add(userMap.get(user));
                listOfData.add(row);
            }
        }

        if (listOfData.isEmpty()) {
            System.err.println("Error: no predictions to be made");
        }

        return listOfData;
    }

    public void convertToCSV(String inputFileName, String outputFileName) throws Exception {
        List<List<String>> listOfData = convertToList(inputFileName);

        try (FileWriter writer = new FileWriter(outputFileName);
             @SuppressWarnings("deprecation")
             CSVPrinter csvPrinter = new CSVPrinter(writer,
                     CSVFormat.DEFAULT.withHeader("song", "user", "predicted rating"))) {

            for (List<String> row : listOfData) {
                csvPrinter.printRecord(row);
            }
        }
    }
}