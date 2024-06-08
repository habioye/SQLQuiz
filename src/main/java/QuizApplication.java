import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizApplication {
    public static String username = "root";
    public static String password = "root";
    public static String url = "jdbc:mysql://localhost:3306/sqlquizdb";


    // Creates a table if it does not exist.
    public static void CreateTable() {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS Questions (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "question VARCHAR(255) NOT NULL UNIQUE," +
                    "answer VARCHAR(255) NOT NULL" +
                    ")";
            statement.execute(createTableSQL);
            System.out.println("Table created successfully.");

        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    // Adds the quiz questions and answers from the array list to the SQL database
    public static void AddQuizQuestions(ArrayList<Quiz> arr) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            for (var quiz : arr) {
                // Create operation
                String createQuery = "INSERT INTO Questions (question, answer) VALUES ( ?, ?)";
                try (PreparedStatement createStatement = connection.prepareStatement(createQuery)) {
//                createStatement.setInt(1, 2);
                    createStatement.setString(1, quiz.question);
                    createStatement.setString(2, quiz.answer);
                    createStatement.executeUpdate();
                    System.out.println("Record created successfully");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Deletes the table if it exists
    public static void DeleteTable() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {// Delete operation
            String deleteTable = "DROP TABLE IF EXISTS Questions";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteTable)) {
                deleteStatement.executeUpdate();
                System.out.println("Record deleted successfully");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Gets the quiz question and answer based on the index.
    public static Quiz getQuiz(int i) {
        Quiz ret = new Quiz();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Read operation
            String readQuery = "SELECT question, answer FROM Questions WHERE id = " + i;
            try (Statement readStatement = connection.createStatement();
                 ResultSet resultSet = readStatement.executeQuery(readQuery)) {
                while (resultSet.next()) {
                    ret.question = resultSet.getString("question");
                    ret.answer = resultSet.getString("answer");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;


    }
    enum Difficulty {
        Easy,
        Medium,
        Hard
    }

    

    // Main Application
    public static void main(String[] args) {


        Quiz q;
        Scanner scanner = new Scanner(System.in);
        String check;


        for (int i = 1; i <= arr.size(); i++) {
            q = getQuiz(i);
            System.out.println("For question "+i+":");
            System.out.println(q.question);
            System.out.println(q.answer);
            System.out.println("---".repeat(5));


            check = scanner.nextLine();
            if (check.equals(q.answer)) {
                System.out.println("You got the question right");
                score+=1;
            } else {
                System.out.println("You got the question wrong. The actual answer is:");
                System.out.println(q.answer);
            }


        }

        System.out.println("You have finished the quiz. You score is "+score+" out of 10!");


    }
}
