
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math4.legacy.ml.distance.EuclideanDistance;

public class UserCalc extends InputCalc {
    /**
     * getUserList gets a list of all the users rating songs
     * 
     * @param fileName name of file
     * @return list of all users
     * @throws IOException in case error is thrown
     */
    public Set<String> getUserList(String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);
        Set<String> userList = new TreeSet<>();

        for (CSVRecord data : CSVdata) {
            userList.add(data.get(1));
        }
        return userList;

    }

    public Map<String, Integer> getNumUserRatings(String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> csvRecords = CSVFormat.EXCEL.parse(in);
        Map<String, Integer> numUserRatingsMap = new HashMap<>();
        
        for (CSVRecord record : csvRecords) {
            String user = record.get(1).trim();
            numUserRatingsMap.put(user, numUserRatingsMap.getOrDefault(user, 0) + 1);
        }
        return numUserRatingsMap;
    }

    /**
     * getUserMean calculates a list of the mean rating for all users
     * 
     * @param fileName name of file
     * @return maps mean rating to user
     * @throws IOException in case error is thrown
     */
    public Map<String, Double> getUserMean(String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);

        // store sum of ratings and count for each song
        Map<String, Integer> userRatingsSum = new HashMap<>();
        Map<String, Integer> userRatingsCount = new HashMap<>();
        Set<String> userList = new TreeSet<>(getUserList(fileName));

        for (CSVRecord data : CSVdata) {
            String user = data.get(1);
            int rating = Integer.parseInt(data.get(2));

            if (!userRatingsSum.containsKey(user)) {
                userList.add(user);
                userRatingsSum.put(user, 0);
                userRatingsCount.put(user, 0);
            }
            userRatingsSum.put(user, userRatingsSum.get(user) + rating);
            userRatingsCount.put(user, userRatingsCount.get(user) + 1);
        }

        // calculate mean ratings
        Map<String, Double> meanList = new HashMap<>();
        for (String user : userList) {
            double mean = (double) userRatingsSum.get(user) / userRatingsCount.get(user);
            meanList.put(user, mean);
        }

        return meanList;
    }

    /**
     * getStandardDeviationOfUser calculates standard deviation of ratings for all
     * users
     * 
     * @param fileName name of file
     * @return maps of standard deviation to user
     * @throws IOException in case error is thrown
     */
    public Map<String, Double> getStandardDeviationOfUser(String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);

        List<String> userList = new ArrayList<>(getUserList(fileName));

        Map<String, Double> userMean = new HashMap<>(getUserMean(fileName));

        // maps to store sum of squared differences and rating counts
        Map<String, Double> sumSquaredDifferences = new HashMap<>();
        Map<String, Integer> userRatingsCount = new HashMap<>();

        for (CSVRecord data : CSVdata) {
            String user = data.get(1).trim();
            double rating = Double.parseDouble(data.get(2));

            double mean = userMean.get(user);
            double squaredDifference = Math.pow(rating - mean, 2);

            // initialize song if not already in map
            sumSquaredDifferences.putIfAbsent(user, 0.0);
            sumSquaredDifferences.put(user, sumSquaredDifferences.get(user) + squaredDifference);

            userRatingsCount.putIfAbsent(user, 0);
            userRatingsCount.put(user, userRatingsCount.get(user) + 1);
        }

        // calculate standard deviation
        Map<String, Double> sdList = new HashMap<>();
        for (String user : userList) {
            int count = userRatingsCount.get(user);
            double variance;

            if (count > 1) {
                variance = sumSquaredDifferences.get(user) / (count);
            } else {
                variance = 0.0;
            }

            double standardDeviation = Math.sqrt(variance);
            sdList.put(user, standardDeviation);
        }

        return sdList;
    }

    // normalizedRating = (rating - mean) / stdDeviation;
    public Map<String, Map<String, Double>> getZScoreCalc(String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);

        Map<String, Double> meanList = new HashMap<>(getUserMean(fileName));
        Map<String, Double> sdList = new HashMap<>(getStandardDeviationOfUser(fileName));
        Map<String, Map<String, Double>> zScoreCalc = new HashMap<>();
        double normalizedRating;

        for (CSVRecord data : CSVdata) {
            String user = data.get(1).trim();
            String song = data.get(0).trim();
            double rating = Double.parseDouble(data.get(2));

            if (sdList.get(user) > 0) {
                normalizedRating = (rating - meanList.get(user)) / sdList.get(user);

                zScoreCalc.putIfAbsent(user, new HashMap<>());
                zScoreCalc.get(user).put(song, normalizedRating);
            }
        }

        // fill in gaps
        InputCalc inputCalc = new InputCalc();
        ArrayList<String> songList = new ArrayList<>(inputCalc.getUniqueSongs(fileName));
        ArrayList<String> userList = new ArrayList<>(getUserList(fileName));

        for (String user : userList) {
            for (String song : songList) {
                if (zScoreCalc.containsKey(user) && !zScoreCalc.get(user).containsKey(song)) {
                    zScoreCalc.get(user).put(song, Double.NEGATIVE_INFINITY);
                }
            }
        }

        return zScoreCalc;
    }

    /**
     * getUserZscoreArray calculates the users z score
     * 
     * @param fileName  name of file
     * @param userName1 name of first user
     * @param userName2 name of second user
     * @return array of users z score
     * @throws IOException in case error is thrown
     */
    public double[] getUserZscoreArray(String fileName, String userName1, String userName2) throws IOException {
        InputCalc inputCalc = new InputCalc();
        Map<String, Map<String, Double>> zScore = getZScoreCalc(fileName);

        Map<String, Double> user1Scores = zScore.get(userName1);
        Map<String, Double> user2Scores = zScore.get(userName2);
        List<Double> validZscores = new ArrayList<>();

        for (String song : inputCalc.getUniqueSongs(fileName)) {

            double user1Zscore = user1Scores.get(song);
            double user2Zscore = user2Scores.get(song);

            // add if neither rating is NEGATIVE_INFINITY
            if (user1Zscore != Double.NEGATIVE_INFINITY && user2Zscore != Double.NEGATIVE_INFINITY) {
                validZscores.add(user1Zscore);
            }

        }

        // convert arraylist to array
        double[] userZscores = new double[validZscores.size()];
        for (int i = 0; i < validZscores.size(); i++) {
            userZscores[i] = validZscores.get(i);
        }

        return userZscores;

    }

    /**
     * userSimilarityCalc calculates the similarity score between all users
     * 
     * @param fileName name of file
     * @return map of user and another user with their similarity calculation
     * @throws Exception in case error is thrown
     */
    public Map<String, Map<String, Double>> userSimilarityCalc(String fileName) throws Exception {
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        Map<String, Map<String, Double>> edOfUsers = new HashMap<>();
        Map<String, Map<String, Double>> zScoreMap = new HashMap<>(getZScoreCalc(fileName));

        try {
            if (zScoreMap.size() < 2) {
                throw new Exception();
            }
            List<String> users = new ArrayList<>(getUserList(fileName));
            String user1 = "";
            String user2 = "";

            for (int i = 0; i < users.size(); i++) {
                for (int j = i + 1; j < users.size(); j++) {
                    user1 = users.get(i);
                    user2 = users.get(j);

                    if (zScoreMap.containsKey(user1) && zScoreMap.containsKey(user2)) {
                        double edCalc = euclideanDistance.compute(getUserZscoreArray(fileName, user1, user2),
                                getUserZscoreArray(fileName, user2, user1));

                        edOfUsers.putIfAbsent(user1, new HashMap<>());
                        edOfUsers.putIfAbsent(user2, new HashMap<>());

                        edOfUsers.get(user1).put(user2, edCalc);
                        edOfUsers.get(user2).put(user1, edCalc);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: less than two cooperative users: " + e.getMessage());
        }

        return edOfUsers;
    }
}