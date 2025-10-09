
import org.apache.commons.math4.legacy.ml.distance.EuclideanDistance;
import java.util.*;

public class Kmeans {

    public static class Cluster {
        public Map<String, Double> centroid;
        public List<String> songs;

        public Cluster(Map<String, Double> centroid) {
            this.centroid = new HashMap<>(centroid);
            this.songs = new ArrayList<>();
        }
    }

    private static double[] mapToArray(Map<String, Double> vector, List<String> userList) {
        double[] array = new double[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            array[i] = vector.getOrDefault(userList.get(i), Double.NEGATIVE_INFINITY); 
        }
        return array;
    }

    private static double computeEuclideanDistance(Map<String, Double> vectorA, Map<String, Double> vectorB, List<String> userList) {
        double[] a = mapToArray(vectorA, userList);
        double[] b = mapToArray(vectorB, userList);
        EuclideanDistance ed = new EuclideanDistance();
        return ed.compute(a, b);
    }

    public static List<Cluster> kMeansClustering(Map<String, Map<String, Double>> normalizedData, List<String> initialSongSelections) {
        List<String> userList = new ArrayList<>(new TreeSet<>(normalizedData.values().iterator().next().keySet()));
        int k = initialSongSelections.size();
        List<Cluster> clusters = new ArrayList<>(k);

        for (String songId : initialSongSelections) {
            if (!normalizedData.containsKey(songId)) {
                throw new IllegalArgumentException("Selected song ID not found in normalized data: " + songId);
            }
            Map<String, Double> seedCentroid = normalizedData.get(songId);
            clusters.add(new Cluster(seedCentroid));
        }

        final int N = 10;
        for (int iteration = 0; iteration < N; iteration++) {
            for (Cluster cluster : clusters) {
                cluster.songs.clear();
            }

            for (Map.Entry<String, Map<String, Double>> entry : normalizedData.entrySet()) {
                String songId = entry.getKey();
                Map<String, Double> songVector = entry.getValue();

                double minDistance = Double.MAX_VALUE;
                int bestClusterIndex = -1;

                for (int c = 0; c < clusters.size(); c++) {
                    Cluster cluster = clusters.get(c);
                    double distance = computeEuclideanDistance(songVector, cluster.centroid, userList);
                    if (distance < minDistance) {
                        minDistance = distance;
                        bestClusterIndex = c;
                    }
                }

                if (bestClusterIndex != -1) {
                    clusters.get(bestClusterIndex).songs.add(songId);
                }
            }

            for (Cluster cluster : clusters) {
                if (cluster.songs.isEmpty()) {
                    continue;
                }

                Map<String, Double> newCentroid = new HashMap<>();
                for (String user : userList) {
                    newCentroid.put(user, 0.0);
                }

                for (String songId : cluster.songs) {
                    Map<String, Double> songVector = normalizedData.get(songId);
                    for (String user : userList) {
                        double rating = songVector.getOrDefault(user, 0.0);
                        newCentroid.put(user, newCentroid.get(user) + rating);
                    }
                }

                int clusterSize = cluster.songs.size();
                for (String user : userList) {
                    newCentroid.put(user, newCentroid.get(user) / clusterSize);
                }

                cluster.centroid = newCentroid;
            }
        }

        return clusters;
    }
}
