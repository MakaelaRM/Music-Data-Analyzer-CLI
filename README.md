# Music Data Analyzer CLI

### About The Project

This project is an interactive Command-Line Interface (CLI) built in Java for analyzing song and user data from CSV files. It provides a user-friendly, menu-driven system to perform various data manipulations and generate insightful statistics without needing to run separate commands for each task. This was developed as a semester-long project for my CS214 class.

### Preview
```
===========================================================
(         __________________________                      )
)        ||                        ||                     (
(        ||                        ||        (  ) )       )
)        ||     Music Analyzer     ||         )( (        (
(        ||          v1.0          ||        (  ) )       )
)        ||                        ||       _________     (
(        ||                        ||    .-'---------|    )
)        *__________________________*   ( c  Java 20 |    (
(       / ==__oooo__==___ooooo-+ o //    `-.         |    )
)      /  oooo   ______  ooooo    //       '_________'    (
(     /         /_____/          /'         `-------'     )
)     `-------------------------'                         (
(                                                         )
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1 - Load Folder
2 - Exit

Select an option: 
```

---

### Features

* **Interactive Menus**: A stateful CLI that guides the user through loading data, selecting files, and choosing analyses.
* **Song Statistics**: Generates overall statistics from the song data in a selected file.
* **User Similarity Analysis**: Compares users based on their listening habits to find similarities.
* **User Listening Prediction**: Predicts a user's future listening patterns.
* **Personalized Recommendations**: Recommends songs to a user based on a selection of their favorites.
* **Robust Error Handling**: Gracefully handles invalid inputs, file errors, and analysis issues without crashing.

---

## Getting Started

To get a local copy up and running, follow these simple steps.

### How to Run

1.  Clone the repository:
    ```sh
    git clone https://github.com/your_username/your_repository_name.git
    ```
2.  Navigate to the project directory:
    ```sh
    cd your_repository_name
    ```
3.  Run the application in interactive mode using the Gradle wrapper:
    ```sh
    ./gradlew run -q --console=plain --args="-i"
    ```

---

## Usage

The application guides you through a series of menus to perform your analysis. The core workflow is to load a folder of CSVs, select a specific file, and then choose an action to perform on that file's data.

#### 1. Main Menu
When the application starts, you can either load a folder containing your CSV data or exit.

```
1 - Load Folder
2 - Exit

Select an option: 1
```

#### 2. Folder and File Selection
After choosing to load a folder, you'll be prompted for the path. Once a valid folder is loaded, you can select a specific file for analysis.

```
Enter folder path: database/files

1 - Select File
2 - Return

Select an option: 1
```
A dynamic list of all files in the directory will be displayed.
```
1 - denorm_greater_than_5.csv
2 - file1.csv
3 - large_dataset.csv
...

Select file number: 2
```

#### 3. Action Menu
After selecting a file, the main action menu appears, allowing you to choose the type of analysis you'd like to perform.

```
1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 1
```

You will then be prompted for an output file name, and the results will be saved. After an action is complete, you are returned to this menu to perform another analysis on the same file.

```
Enter output path: stats.csv

Output written to: stats.csv

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option:
```

---

## Built With

* Java
* Gradle
```
