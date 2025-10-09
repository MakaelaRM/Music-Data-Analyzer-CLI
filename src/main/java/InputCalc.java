
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

public class InputCalc extends ErrorCheck {

    ErrorCheck errorCheck = new ErrorCheck();

    /**
     * getFullSongsList gets a full list of songs and includes duplicates from the
     * csv
     * 
     * @param fileName name of file
     * @return list of songs with duplicates
     * @throws IOException in case error is thrown
     */
    public ArrayList<String> getFullSongsList(String fileName) throws IOException {
        errorCheck.checkFileExists(fileName);
        errorCheck.checkFileIsNotEmpty(fileName);

        ArrayList<String> songs = new ArrayList<>();
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);

        // lists all songs and includes duplicates
        for (CSVRecord data : CSVdata) {
            songs.add(data.get(0).trim()); 
        }

        return songs;
    }

    /**
     * getUniqueSongs gets a list of songs without duplicates from the csv
     * 
     * @param fileName name of file
     * @return list of songs without duplicates
     * @throws IOException in case error is thrown
     */
    public Set<String> getUniqueSongs(String fileName) throws IOException {
        Set<String> songs = new TreeSet<>(); 
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);

        // adds songs to set
        for (CSVRecord data : CSVdata) {
            songs.add(data.get(0).trim()); 
        }

        return songs;
    }

    /**
     * getCountOfSongs gets the number of how many times a song occurs in the csv
     * 
     * @param fileName name of file
     * @return the number of how many times a song occured
     * @throws IOException in case error is thrown
     */
    public List<Integer> getCountOfSong(String fileName) throws IOException {
        List<String> fullSongsList = getFullSongsList(fileName);
        List<String> uniqueSongsList = new ArrayList<>(getUniqueSongs(fileName));
        List<Integer> songCounts = new ArrayList<>();
        
        for (String song : uniqueSongsList) {
            int count = 0;
            for (String fullSong : fullSongsList) {
                if (song.equals(fullSong)) {
                    count++;
                }
            }
            songCounts.add(count);
        }

        return songCounts;
    }

    /**
     * getMeanOfSong Rating gets the mean of the rating of all the songs in the csv
     * 
     * @param fileName name of file
     * @return list of the means of the songs
     * @throws IOException in case error is thrown
     */
    public ArrayList<Double> getMeanOfSongRating(String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);

        ArrayList<String> uniqueSongsList = new ArrayList<>(getUniqueSongs(fileName));

        // store sum of ratings and count for each song
        Map<String, Integer> songRatingsSum = new HashMap<>();
        Map<String, Integer> songRatingsCount = new HashMap<>();

        for (CSVRecord data : CSVdata) {
            String song = data.get(0);
            int rating = Integer.parseInt(data.get(2));

            if (!songRatingsSum.containsKey(song)) {
                songRatingsSum.put(song, 0);
                songRatingsCount.put(song, 0);
            }
            songRatingsSum.put(song, songRatingsSum.get(song) + rating);
            songRatingsCount.put(song, songRatingsCount.get(song) + 1);
        }

        // calculate mean ratings
        ArrayList<Double> meanList = new ArrayList<>();
        for (String song : uniqueSongsList) {
            double mean = (double) songRatingsSum.get(song) / songRatingsCount.get(song);
            meanList.add(mean);
        }

        return meanList;
    }

    /**
     * getStandardDeviationOfRating gets the standard deviation of all the songs in
     * the csv
     * 
     * @param fileName name of file
     * @return list of the standard deviation of song ratings
     * @throws IOException in case error is thrown
     */
    public List<Double> getStandardDeviationOfRating(String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> CSVdata = CSVFormat.EXCEL.parse(in);

        List<String> uniqueSongsList = new ArrayList<>(getUniqueSongs(fileName));

        Map<String, Double> songMean = new HashMap<>();
        List<Double> meanOfRatings = getMeanOfSongRating(fileName);

        for (int i = 0; i < uniqueSongsList.size(); i++) {
            songMean.put(uniqueSongsList.get(i), meanOfRatings.get(i));
        }

        // maps to store sum of squared differences and rating counts
        Map<String, Double> sumSquaredDifferences = new HashMap<>();
        Map<String, Integer> songRatingsCount = new HashMap<>();

        for (CSVRecord data : CSVdata) {
            String song = data.get(0).trim();
            double rating = Double.parseDouble(data.get(2));

            double mean = songMean.get(song);
            double squaredDifference = Math.pow(rating - mean, 2);

            // initialize song if not already in map
            sumSquaredDifferences.putIfAbsent(song, 0.0);
            sumSquaredDifferences.put(song, sumSquaredDifferences.get(song) + squaredDifference);

            songRatingsCount.putIfAbsent(song, 0);
            songRatingsCount.put(song, songRatingsCount.get(song) + 1);
        }

        // calculate standard deviation
        List<Double> sdList = new ArrayList<>();
        for (String song : uniqueSongsList) {
            int count = songRatingsCount.get(song);
            double variance;

            if (count > 1) {
                variance = sumSquaredDifferences.get(song) / (count);
            } else {
                variance = 0.0;
            }

            double standardDeviation = Math.sqrt(variance);
            sdList.add(standardDeviation);
        }

        return sdList;
    }
}