[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/oUE5do52)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=19189679)
# PA6 - User Interface

# Motivation
We have built a CLI (command-line interface) application with multiple features, but requiring users to run a separate command for each task can be inconvenient if they have multiple analyses to perform. To improve the user experience, we will create an **interactive** CLI with a system of menus to allow users to easily perform multiple song/user analyses on multiple files.

# Tasks
- Implement an interactive CLI that allows users to interact with the existing application through menus and prompts.

## Menus
The interactive CLI will contain several menus that are displayed depending on the application state. To make the application stateful, you should use `while` loops. To get user input from `System.in` you can use `java.util.Scanner`.


The three core menus are the [first menu](#first-menu), [second menu](#second-menu), and [action menu](#action-menu). While there are additional menus, these three should serve as the main **loops** in your program. If a user makes an invalid choice or an error occurs before reaching a different core menu, they should be returned to the most recent of these three menus they interacted with.


### Splash
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

Above is the first menu that the user sees when the application starts, with a prompt for the user to enter the number corresponding with a menu option. Above the menu is an example of a [**splash**](https://en.wikipedia.org/wiki/Splash_screen). In modern applications, a splash screen usually displays a logo or animation when an app launches, and if loading is required may double as a loading screen. 


You’re welcome to use this splash or create your own, as it will be ignored during autograding. The output shown in this README (excluding the splash) is the minimum requirement. Your menus and prompts should match ours line-for-line, however feel free to include white space, extra newlines, or extra content between our expected lines. You can add messages between expected outputs, such as printing the splash before each menu or adding a message after a user makes a selection. You're allowed to add extra printed messages, but do not add extra input prompts, as they change the program's behavior.


Do not change the program’s overall behavior. Given the same sequence of inputs, your program should behave exactly the same as ours, with all expected menus and prompts (ignoring extra content you choose to print) appearing in the correct order in the console, and output files produced appropriately. 


If you want to keep it simple, you can print your splash once at the beginning of the program, and just match what is given in the provided outputs. If you want to get creative, you can play with `Thread.sleep()` to animate your splash on start-up, but be aware that the autograder has a timeout of 30 seconds. You can use [ANSI](https://www.w3schools.blog/ansi-colors-java) escape codes for color, or to "clear" the screen between menus:

![Demo](https://i.imgur.com/d8KPiGe.gif)


### First Menu
```
1 - Load Folder
2 - Exit

Select an option: 
```
If the user selects option `2` the application stops. If the user selects option `1`, they will be prompted to enter a folder path before they are shown the [second menu](#second-menu).

### Second Menu
Once the user enters a valid folder path, they are shown the second menu, with an option to select a file or return to [first menu](#first-menu).
```
1 - Load Folder
2 - Exit

Select an option: 1

Enter folder path: database/files

1 - Select File
2 - Return

Select an option: 
```

If the users selects option `2` they should be returned to the [first menu](#splash-and-first-menu).
```
1 - Select File
2 - Return

Select an option: 2

1 - Load Folder
2 - Exit

Select an option: 
```
If the user selects option `1` they will be shown a menu consisting of the filenames of each file in the provided folder **in lexicographical order**. Note that this menu's **size is dynamic** and depends on how many files are in the provided folder.
```
1 - Select File
2 - Return

Select an option: 1

1 - denorm_greater_than_5.csv
2 - file1.csv
3 - file2.csv
4 - file3.csv
5 - file4.csv
6 - file5.csv
7 - large_dataset.csv
8 - one_song.csv
9 - one_user.csv

Select file number: 
```

### Action Menu
Upon selecting a file, the user should be brought to the actions menu listing all of the things they can analyze about the file. These are the features you implemented over the past several weeks. List the actions in the order shown below.
```
1 - denorm_greater_than_5.csv
2 - file1.csv
3 - file2.csv
4 - file3.csv
5 - file4.csv
6 - file5.csv
7 - large_dataset.csv
8 - one_song.csv
9 - one_user.csv

Select file number: 2

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 
```

If the user selects option `5` they should be returned to the [second menu](#second-menu):
```
1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 5

1 - Select File
2 - Return

Select an option: 
```

If the user selects any of the options (except for `User Recommendation`, see later), the user is prompted for an output file name and the application writes the results to a CSV file and prints `Output written to: <filename>.csv`. If the user chooses the **same** output file name for multiple different options then **overwrite** that same output file. Then the user is returned to the [action menu](#action-menu) to perform more actions with the same selected file:
```
1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 1

Enter output path: stats.csv

Output written to: stats.csv

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 
```

### Song Selection for User Recommendation
For the feature `User Recommendation`, we will have an extra menu for the user to select songs that exist in the file. List all unique song titles from the selected file in **lexicographical order**:
```
1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 4

Enter output path: rec.csv

1 - song1
2 - song2
3 - song3
4 - song4
5 - song5
6 - song6

Enter selections (e.g. 2,5,7): 3,6,5

Output written to: rec.csv

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 
```

# Error Handling
- Interactive mode does not stop when an error occurs.
- Error messages will be printed to `System.out` while in interactive mode. As before, the message can say anything as long as it starts with `Error`.
- All features (Song Stats, User Similarity, User Prediction, User Recommendation) behave the same as before with their respective error cases. If an error for a feature occurs, display the corresponding message *after* the user has entered the output file name.
- If the user makes an invalid selection at any point during interactive mode, display an error message and return to the appropriate menu ([first menu](#first-menu), [second menu](#second-menu), or [action menu](#action-menu), see examples).

## Some Error Examples (see [Example 2](#example-2) for more)
### Invalid option
```
1 - Load Folder
2 - Exit

Select an option: a

Error: invalid option

1 - Load Folder
2 - Exit

Select an option: 
```

### Folder Not Found
```
1 - Load Folder
2 - Exit

Select an option: 1

Enter folder path: qwerty

Error: folder not found

1 - Load Folder
2 - Exit

Select an option: 
```

### Provided Folder Is Empty
```
1 - Load Folder
2 - Exit

Select an option: 1

Enter folder path: empty

Error: no files in folder

1 - Load Folder
2 - Exit

Select an option: 
```

### Feature Error
```
1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 2

Enter output path: sim.csv

Error: must have at least two cooperative users for user similarity

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 
```

# Running Your Program
Your program can still run any of the features as before with program arguments. Now if the arg `-i` is passed to your program, it should start **interactive** mode. You can pass the arg `--console=plain` to gradle to get rid of the gradle `Executing` prompt.

# Example Output
Command:
`gradle run -q --console=plain --args="-i"`

## Example 1
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

Select an option: 1

Enter folder path: database/files

1 - Select File
2 - Return

Select an option: 1

1 - denorm_greater_than_5.csv
2 - file1.csv
3 - file2.csv
4 - file3.csv
5 - file4.csv
6 - file5.csv
7 - large_dataset.csv
8 - one_song.csv
9 - one_user.csv

Select file number: 4

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 1

Enter output path: stats.csv

Output written to: stats.csv

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 4

Enter output path: rec.csv

1 - song1
2 - song2
3 - song3
4 - song4
5 - song5
6 - song6

Enter selections (e.g. 2,5,7): 2,1,4

Output written to: rec.csv

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 5

1 - Select File
2 - Return

Select an option: 1

1 - denorm_greater_than_5.csv
2 - file1.csv
3 - file2.csv
4 - file3.csv
5 - file4.csv
6 - file5.csv
7 - large_dataset.csv
8 - one_song.csv
9 - one_user.csv

Select file number: 9

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 2

Enter output path: sim.csv

Error: must have at least two cooperative users for user similarity

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 6

Error: invalid option

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 5

1 - Select File
2 - Return

Select an option: 2

1 - Load Folder
2 - Exit

Select an option: 2

```

## Example 2
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

Select an option: a

Error: invalid option

1 - Load Folder
2 - Exit

Select an option: 1

Enter folder path: qwerty

Error: folder not found

1 - Load Folder
2 - Exit

Select an option: 1

Enter folder path: database/files

1 - Select File
2 - Return

Select an option: 3

Error: invalid option

1 - Select File
2 - Return

Select an option: 1

1 - denorm_greater_than_5.csv
2 - file1.csv
3 - file2.csv
4 - file3.csv
5 - file4.csv
6 - file5.csv
7 - large_dataset.csv
8 - one_song.csv
9 - one_user.csv

Select file number: 10

Error: invalid option

1 - Select File
2 - Return

Select an option: 1

1 - denorm_greater_than_5.csv
2 - file1.csv
3 - file2.csv
4 - file3.csv
5 - file4.csv
6 - file5.csv
7 - large_dataset.csv
8 - one_song.csv
9 - one_user.csv

Select file number: 7

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: sim

Error: invalid option

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 3

Enter output path: prediction.txt

Error: only `.csv` files are supported

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 3

Enter output path: prediction.csv

Output written to: prediction.csv

1 - Song Stats
2 - User Similarity
3 - User Prediction
4 - User Recommendation
5 - Return

Select an option: 5

1 - Select File
2 - Return

Select an option: 2

1 - Load Folder
2 - Exit

Select an option: 2

```

# Submitting Your Homework
Your submission must be in the main branch of your GitHub repository.

# Grading Your Homework
We will pull your code from your repository. It must contain, at a minimum two Java files. One must be named `Cs214Project.java`. We will run your program by starting it with that class name. The second file must be named `TestCs214Project.java`. This will be used to run your JUnit tests. All future assignments must contain these two files, although they may contain different code as needed by the particulars of that assignment.

# Hints
- Use `while` loops for the core menus. 
- `switch/case` could be useful for handling menu selections. You can take advantage of [switch expressions](https://docs.oracle.com/en/java/javase/17/language/switch-expressions-and-statements.html), which aren't as bulky as traditional switch statements.
- Print statements could muddy up your code pretty quickly. Think about using methods to prompt and print menus.
- You can pipe user input to your program all at once: `echo -e "1\ndatabase/files\n2\n2\n" | gradle run -q --console=plain --args="-i"`. Just know that this way the input is buffered and available at once, so `java.util.Scanner` methods consume it without waiting.
- You can simulate `System.in` input in your JUnit tests with something like the following. This is an example, and much of this could be modularized to be shared between multiple tests. The input is non-blocking just like when you pipe input to your program:
```java
    @Test
    public void testInteractiveMode() {
        String input = "1\ndatabase/files\n2\n2\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(out));

            Cs214Project.main(new String[] { "-i" });

            String console = out.toString();
            assertTrue(console.contains("Music Analyzer"));
            assertTrue(console.contains("Load Folder"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
```

# Policies

All work you submit must be your own. You may not submit code written by a peer, a former student, or anyone else. You may not copy or buy code from the web. Use of generative AI tools such as ChatGPT, Claude, GitHub CoPilot, or other LLMs is prohibited. The department academic integrity policies apply.

There is a 3 day late period for this assignment but each day it is late your grade will receive a 10% deduction.
