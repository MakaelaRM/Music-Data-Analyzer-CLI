
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Predict extends UserCalc {

    public Map<String, ArrayList<String>> mostSimilarList(String fileName) throws Exception {
        Map<String, ArrayList<String>> mostSimilar = new HashMap<>();
        TreeMap<String, Map<String, Double>> userSimilarityMap = new TreeMap<>(userSimilarityCalc(fileName));

        Set<String> userSet = userSimilarityMap.keySet();
        ArrayList<String> users = new ArrayList<>();
        for (String s : userSet) {
            users.add(s);
        }

        for (int i = 0; i < users.size(); i++) {
            String user1 = users.get(i);
            ArrayList<String> userList = new ArrayList<>();
            for (int j = 0; j < users.size(); j++) {
                if (i != j) {
                    userList.add(users.get(j));
                }
            }
            mostSimilar.put(user1, userList);
        }
        return mostSimilar;
    }

    public Map<String, Map<String, Double>> predictMissingRatings(String fileName) throws Exception {
        Map<String, Map<String, Double>> normalizedRatingMap = getZScoreCalc(fileName);
        Map<String, ArrayList<String>> mostSimilarMap = mostSimilarList(fileName);
        Map<String, Double> meanMap = getUserMean(fileName);
        Map<String, Double> sdMap = getStandardDeviationOfUser(fileName);
        List<String> users = new ArrayList<>(getUserList(fileName));
        List<String> songs = new ArrayList<>(getUniqueSongs(fileName));
        Map<String, Map<String, Double>> predictedMap = new HashMap<>();

        for (String user : users) {
            predictedMap.put(user, new HashMap<>());
        }

        for (String targetUser : users) {
            for (String song : songs) {
                Map<String, Double> targetRatings = normalizedRatingMap.get(targetUser);
                if (targetRatings == null) {
                    continue;
                }
                Double targetUserRating = targetRatings.get(song);
                if (targetUserRating == null) {
                    continue;
                }

                if (targetUserRating.equals(Double.NEGATIVE_INFINITY)) {
                    ArrayList<String> similarUsers = mostSimilarMap.get(targetUser);
                    double borrowedRating = Double.NEGATIVE_INFINITY;
                    int count = 0;

                    while (count < similarUsers.size()) {
                        String otherUser = similarUsers.get(count);
                        Map<String, Double> otherUserRatings = normalizedRatingMap.get(otherUser);
                        if (otherUserRatings != null) {
                            Double otherUserRating = otherUserRatings.get(song);
                            if (otherUserRating != null && !otherUserRating.equals(Double.NEGATIVE_INFINITY)) {
                                boolean commonRated = false;
                                for (String s : songs) {
                                    Double target = normalizedRatingMap.get(targetUser).get(s);
                                    Double other = otherUserRatings.get(s);
                                    if (target != null && other != null &&
                                            !target.equals(Double.NEGATIVE_INFINITY) &&
                                            !other.equals(Double.NEGATIVE_INFINITY)) {
                                        commonRated = true;
                                        break;
                                    }
                                }
                                if (commonRated) {
                                    borrowedRating = otherUserRating;
                                    break;
                                }
                            }
                        }
                        count++;
                    }

                    double predictedRating;
                    if (borrowedRating == Double.NEGATIVE_INFINITY) {
                        predictedRating = Double.NaN;
                    } else {
                        predictedRating = Math.round(borrowedRating * sdMap.get(targetUser) + meanMap.get(targetUser));
                        if (predictedRating > 5)
                            predictedRating = 5;
                        if (predictedRating < 1)
                            predictedRating = 1;
                    }

                    predictedMap.get(targetUser).put(song, predictedRating);
                }
            }
        }

        Map<String, Map<String, Double>> songToUserMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> userEntry : predictedMap.entrySet()) {
            String user = userEntry.getKey();
            for (Map.Entry<String, Double> songEntry : userEntry.getValue().entrySet()) {
                String song = songEntry.getKey();
                Double rating = songEntry.getValue();
                songToUserMap.computeIfAbsent(song, k -> new HashMap<>()).put(user, rating);
            }
        }

        return songToUserMap;
    }
}