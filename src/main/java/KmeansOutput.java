
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class KmeansOutput extends PrepareDataForClustering {

    public List<List<String>> convertToList(String fileName, List<String> seeds) throws Exception {
        Map<String, Map<String, Double>> normalizedData = renormalizeData(fileName);

        Map<String, Map<String, Double>> filteredData = new TreeMap<>();
        for (String key : normalizedData.keySet()) {
            if (key.toLowerCase().startsWith("song")) {
                filteredData.put(key, normalizedData.get(key));
            }
        }

        if (filteredData.size() <= seeds.size()) {
            throw new IllegalArgumentException("Error: not enough data for clustering. Provide a larger dataset or fewer seeds.");
        }

        for (String seed : seeds) {
            if (!filteredData.containsKey(seed)) {
                throw new IllegalArgumentException("Error: selected seed '" + seed + "' not found in dataset.");
            }

            Map<String, Double> ratings = filteredData.get(seed);
            TreeSet<Double> distinctRatings = new TreeSet<>(ratings.values());

            if (distinctRatings.size() <= 1) {
                throw new IllegalArgumentException("Error: selected song '" + seed + "' must have more than one distinct rating");
            }
        }

        List<Kmeans.Cluster> clusters = Kmeans.kMeansClustering(filteredData, seeds);

        TreeMap<String, TreeSet<String>> sortedData = new TreeMap<>();
        boolean foundAnyRecommendations = false;

        for (int i = 0; i < clusters.size(); i++) {
            String seed = seeds.get(i);
            Kmeans.Cluster cluster = clusters.get(i);

            TreeSet<String> recommendations = new TreeSet<>();

            for (String song : cluster.songs) {
                if (!seeds.contains(song) && song.toLowerCase().startsWith("song")) {
                    recommendations.add(song);
                }
            }

            if (!recommendations.isEmpty()) {
                sortedData.put(seed, recommendations);
                foundAnyRecommendations = true;
            }
        }

        if (!foundAnyRecommendations) {
            throw new IllegalArgumentException("Error: no songs to recommend. Songs may have been removed. Try with a larger file or fewer selections.");
        }

        List<List<String>> listOfData = new ArrayList<>();
        for (String choice : sortedData.keySet()) {
            for (String recommendation : sortedData.get(choice)) {
                List<String> row = new ArrayList<>();
                row.add(choice);
                row.add(recommendation);
                listOfData.add(row);
            }
        }

        return listOfData;
    }

    @SuppressWarnings("deprecation")
    public void convertToCSV(String inputFileName, String outputFileName, List<String> seeds) throws Exception {
        List<List<String>> listOfData = convertToList(inputFileName, seeds);

        try (FileWriter writer = new FileWriter(outputFileName);
             CSVPrinter csvPrinter = new CSVPrinter(writer,
                     CSVFormat.DEFAULT.withHeader("user choice", "recommendation"))) {

            for (List<String> row : listOfData) {
                csvPrinter.printRecord(row);
            }
        }
    }
}