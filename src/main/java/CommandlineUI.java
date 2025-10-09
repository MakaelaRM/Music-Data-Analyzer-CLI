import java.io.File;
import java.util.*;

public class CommandlineUI {
    public final Scanner scanner;

    public CommandlineUI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printSplash();
        firstMenu();
    }

    public void printSplash() {
        System.out.println("(         __________________________                      )");
        System.out.println(")        ||                        ||                     (");
        System.out.println("(        ||                        ||        (  ) )       )");
        System.out.println(")        ||     Music Analyzer     ||         )( (        (");
        System.out.println("(        ||          v1.0          ||        (  ) )       )");
        System.out.println(")        ||                        ||       _________     (");
        System.out.println("(        ||                        ||    .-'---------|    )");
        System.out.println(")        *__________________________*   ( c  Java 20 |    (");
        System.out.println("(       / ==__oooo__==___ooooo-+ o //    `-.         |    (");
        System.out.println(")      /  oooo   ______  ooooo    //       '_________'    (");
        System.out.println("(     /         /_____/          /'         `-------'     )");
        System.out.println(")     `-------------------------'                         (");
        System.out.println("(                                                         )");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
    }

    public void firstMenu() {
        while (true) {
            System.out.println("1 - Load Folder");
            System.out.println("2 - Exit");
            System.out.println();
            System.out.print("Select an option: ");
            String input = scanner.nextLine().trim();

            if ("1".equals(input)) {
                System.out.println();
                System.out.print("Enter folder path: ");
                String path = scanner.nextLine().trim();
                File folder = new File(path);

                if (!folder.exists() || !folder.isDirectory()) {
                    System.out.println("Error: folder not found");
                    System.out.println();
                    continue;
                }

                File[] rawFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
                if (rawFiles == null || rawFiles.length == 0) {
                    System.out.println("Error: no files in folder");
                    System.out.println();
                    continue;
                }

                List<File> files = Arrays.asList(rawFiles);
                files.sort(Comparator.comparing(File::getName));
                System.out.println();
                secondMenu(files);

            } else if ("2".equals(input)) {
                break;

            } else {
                System.out.println("Error: invalid option");
                System.out.println();
            }
        }
    }

    public void secondMenu(List<File> files) {
        while (true) {
            System.out.println("1 - Select File");
            System.out.println("2 - Return");
            System.out.println();
            System.out.print("Select an option: ");
            String input = scanner.nextLine().trim();

            if ("1".equals(input)) {
                System.out.println();
                for (int i = 0; i < files.size(); i++) {
                    System.out.printf("%d - %s%n", i + 1, files.get(i).getName());
                }
                System.out.println();
                System.out.print("Select file number: ");
                String selection = scanner.nextLine().trim();
                int fileIndex;
                try {
                    fileIndex = Integer.parseInt(selection);
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid option");
                    System.out.println();
                    continue;
                }
                if (fileIndex < 1 || fileIndex > files.size()) {
                    System.out.println("Error: invalid option");
                    System.out.println();
                    continue;
                }
                System.out.println();
                actionMenu(files.get(fileIndex - 1));

            } else if ("2".equals(input)) {
                System.out.println();
                return;

            } else {
                System.out.println("Error: invalid option");
                System.out.println();
            }
        }
    }

    public void actionMenu(File file) {
        while (true) {
            System.out.println("1 - Song Stats");
            System.out.println("2 - User Similarity");
            System.out.println("3 - User Prediction");
            System.out.println("4 - User Recommendation");
            System.out.println("5 - Return");
            System.out.println();
            System.out.print("Select an option: ");
            String input = scanner.nextLine().trim();
            int actionIndex;
            try {
                actionIndex = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid option");
                System.out.println();
                continue;
            }

            if (actionIndex == 5) {
                System.out.println();
                return;
            }
            if (actionIndex < 1 || actionIndex > 5) {
                System.out.println("Error: invalid option");
                System.out.println();
                continue;
            }

            System.out.println();
            System.out.print("Enter output path: ");
            String outputPath = scanner.nextLine().trim();
            if (!outputPath.endsWith(".csv")) {
                System.out.println("Error: only `.csv` files are supported");
                System.out.println();
                continue;
            }

            try {
                switch (actionIndex) {
                    case 1:
                        runSongStats(file.getAbsolutePath(), outputPath);
                        break;
                    case 2:
                        runUserSimilarity(file.getAbsolutePath(), outputPath);
                        break;
                    case 3:
                        runUserPrediction(file.getAbsolutePath(), outputPath);
                        break;
                    case 4:
                        runUserRecommendation(file.getAbsolutePath(), outputPath);
                        break;
                }
                System.out.println("Output written to: " + outputPath);
                System.out.println();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println();
            }
        }
    }

    public void runSongStats(String inputPath, String outputPath) throws Exception {
        new OutputCSV().convertToCSV(inputPath, outputPath);
    }

    public void runUserSimilarity(String inputPath, String outputPath) throws Exception {
        new UserCalcOutput().convertToCSV(inputPath, outputPath);
    }

    public void runUserPrediction(String inputPath, String outputPath) throws Exception {
        new PredictOutput().convertToCSV(inputPath, outputPath);
    }

    public void runUserRecommendation(String inputPath, String outputPath) throws Exception {
        List<String> songs = getUniqueSongTitles(inputPath);
        for (int i = 0; i < songs.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, songs.get(i));
        }
        System.out.println();
        System.out.print("Enter selections (e.g. 2,5,7): ");
        String line = scanner.nextLine().trim();
        String[] parts = line.split(",");
        List<String> selected = new ArrayList<>();
        for (String part : parts) {
            int songIndex = Integer.parseInt(part.trim());
            if (songIndex < 1 || songIndex > songs.size()) {
                System.out.println("Error: invalid option");
                System.out.println();
                return;
            }
            selected.add(songs.get(songIndex - 1));
        }
        new KmeansOutput().convertToCSV(inputPath, outputPath, selected);
    }

    public List<String> getUniqueSongTitles(String inputPath) throws Exception {
        return new ArrayList<>(new InputCalc().getUniqueSongs(inputPath));
    }
}