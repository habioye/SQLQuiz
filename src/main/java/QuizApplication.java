import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizApplication {
    public static String username = "root";
    public static String password = "root";
    public static String url = "jdbc:mysql://localhost:3306/sqlquizdb";


    enum Difficulty {
        Easy,
        Medium,
        Hard
    }

    private static ArrayList<Quiz> getQuizes(Difficulty d) {
        String tableName;
        switch (d) {
            case Easy -> {
                tableName = "Easy";
            }
            case Medium -> {
                tableName = "Medium";

            }
            default -> {
                tableName = "Hard";
            }
        }
        ArrayList<Quiz> ret = new ArrayList<>();
        Quiz q;
        String readQuery = "Select Question, Answer From " + tableName;
        try (Connection connection = DriverManager.getConnection(url,username, password)) {
            try (Statement readStatement = connection.createStatement()) {
                ResultSet resultSet = readStatement.executeQuery(readQuery);
                while (resultSet.next()) {
                    q = new Quiz();
                    q.question = resultSet.getString("Question");
                    q.answer = resultSet.getString("answer");
                    q.answerLower = q.answer.toLowerCase();
                    ret.add(q);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;


    }
    public static String getTable(Difficulty d) {
        String table;
        switch (d) {
            case Difficulty.Easy -> {
                table = "easy";
            }
            case Difficulty.Medium -> {
                table = "medium";
            }
            default -> {
                table = "hard";
            }
        }
        return table;
    }

    public static String getLeaderBoardTable(Difficulty d) {
        String table;
        switch (d) {
            case Difficulty.Easy -> {
                table = "easyleaderboard";
            }
            case Difficulty.Medium -> {
                table = "mediumleaderboard";
            }
            default -> {
                table = "hardleaderboard";
            }
        }
        return table;
    }
    public static void insertLeaderBoard(LeaderBoard leaderBoard) {
        String table = getLeaderBoardTable(leaderBoard.d);

    try(Connection connection = DriverManager.getConnection(url,username,password)) {
        String createQuery = "INSERT INTO "+table+" (name, score) VALUES (?, ?)";
        try (PreparedStatement createStatement = connection.prepareStatement(createQuery)){
            createStatement.setString(1,leaderBoard.name);
            createStatement.setInt(2,leaderBoard.score);
            createStatement.executeUpdate();
            System.out.println("Leaderboard Record Created Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    public static ArrayList<LeaderBoard> getLeaderBoard(Difficulty d) {
        ArrayList<LeaderBoard> ret = new ArrayList<>();
        LeaderBoard l;
        String table = getLeaderBoardTable(d);
        String name;
        int score;
        try (Connection connection = DriverManager.getConnection(url,username,password)) {
            String readQuery = "SELECT name, score FROM "+table+" ORDER BY score DESC";
            try(Statement readStatment = connection.createStatement();
            ResultSet resultSet = readStatment.executeQuery(readQuery)) {
                while (resultSet.next()) {
                    name = resultSet.getString("name");
                    score = resultSet.getInt("score");

                    l = new LeaderBoard(d,name,score);
                    ret.add(l);

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void displayLeaderBoard(Difficulty d) {
        ArrayList<LeaderBoard> arr = getLeaderBoard(d);
        String table = getTable(d);
        // Print header
        System.out.println("+---------------------+------------+");
        System.out.println("| " + String.format("%-33s", getTable(d).toUpperCase()+" " + "LEADERBOARD") + " |");

        System.out.println("+---------------------+------------+");
    System.out.println("| " + String.format("%-20s", "Name") + " | " + String.format("%-10s", "Score") + " |");
        System.out.println("+---------------------+------------+");

        // Print each entry in the leaderboard
        for (LeaderBoard l : arr) {
            String name = l.name;
            int score = l.score;
            System.out.println("| " + String.format("%-20s", name) + " | " + String.format("%-10d", score) + " |");
        }

        // Print footer
        System.out.println("+---------------------+------------+");
    }

    public static void RunGame() {

        Difficulty d;
        Quiz q;
        Scanner scanner = new Scanner(System.in);
        String check;
        int choice;
        int score = 0;
        String name;
        while (true) {
            System.out.println("""
                    Select your difficulty
                    1. Easy
                    2. Medium
                    3. Hard
                    """);
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    d = Difficulty.Easy;
                }
                case 2 -> {
                    d = Difficulty.Medium;
                }
                case 3 -> {
                    d = Difficulty.Hard;
                }
                default -> {
                    System.out.println("You did not select a valid choice");
                    continue;
                }
            }

            ArrayList<Quiz> arr = getQuizes(d);
            for (int i = 1; i <= arr.size(); i++) {
                q = arr.get(i - 1);
                System.out.println("For question " + i + ":");
                System.out.println(q.question);
                System.out.println("---".repeat(5));


                check = scanner.nextLine().toLowerCase();
                if (check.equals(q.answerLower)) {
                    System.out.println("You got the question right");
                    score += 1;
                } else {
                    System.out.println("You got the question wrong. The actual answer is:");
                    System.out.println(q.answer);
                }
            }
            System.out.println("You have finished the quiz. You score is " + score + " out of 10!");
            System.out.println("Enter your name for the leaderboard");
            while (true) {
                name = scanner.nextLine();
                if (name.isBlank() || name.isEmpty()) {
                    System.out.println("Invalid Name. Make sure to insert your name");
                } else break;
            }
            LeaderBoard leaderBoard = new LeaderBoard(d,name,score);
            insertLeaderBoard(leaderBoard);
            displayLeaderBoard(d);
            return;


        }
    }
    public static void ViewLeaderBoard() {
        System.out.println("Which Leaderboard do you want to view");
        int choice;
        Scanner scanner = new Scanner(System.in);
        Difficulty d;
        while (true) {
            System.out.println("""
                    1. Easy
                    2. Medium
                    3. Hard
                    """);
            choice = scanner.nextInt();

            switch (choice) {
                case 1-> {
                    d = Difficulty.Easy;
                }
                case 2 -> {
                    d = Difficulty.Medium;
                }
                case 3 -> {
                    d = Difficulty.Hard;
                }
                default -> {
                    System.out.println("You chose an invalid option");
                    continue;
                }

            }
            displayLeaderBoard(d);
            return;
        }
    }
    public static void displayDirections() {
        System.out.println("""
                This is a quiz game meant to test your knowledge in SQL.
                You will be given questions and you have to answer those questions.
                If you get you get a question right you will get a point.
                You can choose difficulty and view the leaderboard.
                """);
    }
    public static void displayMainMenu() {
        System.out.println("""
                1.) Start Game
                2.) View LeaderBoard
                3.) View Directions
                4.) Exit
                """);
    }

    // Main Application
    public static void main(String[] args) {


        System.out.println("""
                SQL Quiz Game
                ------------------------------
                """);


        displayDirections();

        Quiz q;
        Scanner scanner = new Scanner(System.in);
        String check;
        int choice;
        boolean invalid;
        int score;
        while (true) {
            displayMainMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    RunGame();
                }
                case 2 -> {
                    ViewLeaderBoard();
                }
                case 3 -> {
                    displayDirections();
                }
                case 4 -> {
                    System.out.println("Exiting Game");
                    return;

                }
                default -> {
                    System.out.println("You selected an invalid option!");
                }
            }




        }



    }
}
