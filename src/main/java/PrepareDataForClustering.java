
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PrepareDataForClustering extends Predict {

    public static Map<String, Map<String, Double>> readRatings(String filename) {
        Map<String, Map<String, Double>> songRatings = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 3) continue;
                String song = parts[0].trim();
                String user = parts[1].trim();
                double rating = Double.parseDouble(parts[2].trim());
                songRatings.computeIfAbsent(song, k -> new HashMap<>()).put(user, rating);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songRatings;
    }

    public Map<String, Map<String, Double>> fillInData(String fileName) throws Exception {
        Map<String, Map<String, Double>> original = readRatings(fileName);
        Map<String, Map<String, Double>> predicted = predictMissingRatings(fileName);
    
        Map<String, Map<String, Double>> filled = new HashMap<>();
    
        for (String song : original.keySet()) {
            filled.put(song, new HashMap<>(original.get(song)));
        }
    
        for (String song : predicted.keySet()) {
            filled.putIfAbsent(song, new HashMap<>());
            for (Map.Entry<String, Double> entry : predicted.get(song).entrySet()) {
                filled.get(song).putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    
        return filled;
    }
    

    public Map<String, Map<String, Double>> completeData(String fileName) throws Exception {
        Map<String, Map<String, Double>> original = readRatings(fileName);
        Map<String, Map<String, Double>> data = fillInData(fileName);

        Map<String, Double> songSum = new HashMap<>();
        Map<String, Integer> songCount = new HashMap<>();
        Map<String, Double> userSum = new HashMap<>();
        Map<String, Integer> userCount = new HashMap<>();

        for (String song : original.keySet()) {
            for (Map.Entry<String, Double> entry : original.get(song).entrySet()) {
                String user = entry.getKey();
                double rating = entry.getValue();
                songSum.put(song, songSum.getOrDefault(song, 0.0) + rating);
                songCount.put(song, songCount.getOrDefault(song, 0) + 1);
                userSum.put(user, userSum.getOrDefault(user, 0.0) + rating);
                userCount.put(user, userCount.getOrDefault(user, 0) + 1);
            }
        }

        Map<String, Double> songMean = new HashMap<>();
        for (String song : songSum.keySet()) {
            songMean.put(song, songSum.get(song) / songCount.get(song));
        }
        Map<String, Double> userMean = new HashMap<>();
        for (String user : userSum.keySet()) {
            userMean.put(user, userSum.get(user) / userCount.get(user));
        }

        Set<String> allUsers = new TreeSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    allUsers.add(parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String song : data.keySet()) {
            Map<String, Double> ratings = data.get(song);
            ratings.keySet().retainAll(allUsers);
            for (String user : allUsers) {
                if (!ratings.containsKey(user) || Double.isNaN(ratings.get(user))) {
                    double sMean = songMean.getOrDefault(song, 3.0);
                    double uMean = userMean.getOrDefault(user, 3.0);
                    int sCount = songCount.getOrDefault(song, 0);
                    int uCount = userCount.getOrDefault(user, 0);
                    int total = sCount + uCount;
                    double filled = (total > 0) ? Math.round((sMean * sCount + uMean * uCount) / total) : 3.0;
                    filled = Math.max(1.0, Math.min(5.0, filled));
                    ratings.put(user, filled);
                }
            }
        }

        return data;
    }

    public Map<String, Map<String, Double>> renormalizeData(String fileName) throws Exception {
        Map<String, Map<String, Double>> data = completeData(fileName);
        Map<String, Map<String, Double>> norm = new HashMap<>();
        for (String song : data.keySet()) {
            Map<String, Double> ratings = data.get(song);
            double mean = ratings.values().stream().mapToDouble(v -> v).average().orElse(0);
            double std = Math.sqrt(ratings.values().stream().mapToDouble(v -> Math.pow(v - mean, 2)).average().orElse(0));
            Map<String, Double> normed = new HashMap<>();
            for (String user : ratings.keySet()) {
                double value = (std == 0.0) ? 0.0 : (ratings.get(user) - mean) / std;
                normed.put(user, value);
            }
            norm.put(song, normed);
        }
        return norm;
    }
}