import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizApplication {
    public static String username = "root";
    public static String password = "root";
    public static String url = "jdbc:mysql://localhost:3306/sqlquizdb";


    public static void CreateTable() {
//        String url = "jdbc:mysql://localhost:3306/sqlQuizDB";
//        String username = "root";
//        String password = "password";


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

    public static void AddQuizQuestions(ArrayList<Quiz> arr) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            for (var quiz : arr) {
                // Create operation
                String createQuery = "INSERT INTO Questions (id, question, answer) VALUES ( ?, ?)";
                try (PreparedStatement createStatement = connection.prepareStatement(createQuery)) {
//                createStatement.setInt(1, 2);
                    createStatement.setString(2, quiz.question);
                    createStatement.setString(3, quiz.answer);
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

    public static Quiz getQuiz(int i) {
        Quiz ret = new Quiz();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Read operation
            String readQuery = "SELECT question, answer FROM users WHERE id = " + i;
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

    public static void main(String[] args) {
        DeleteTable();
        CreateTable();
        int score = 0;

        // Set up The Quiz Questions and Answers
        ArrayList<Quiz> arr = new ArrayList<>();
        Quiz q1 = new Quiz();
        q1.question = "Retrieve all columns and rows from a table named employees.";
        q1.answer = "SELECT * FROM employees;";
        Quiz q2 = new Quiz();
        q2.question = "Get the first_name and last_name of all employees from the employees table who work in the Sales department.";
        q2.answer = "SELECT first_name, last_name FROM employees WHERE department = 'Sales';\n";
        Quiz q3 = new Quiz();
        q3.question = "Find all products from the products table that have a price greater than 100.";
        q3.answer = "SELECT * FROM products WHERE price > 100;";
        Quiz q4 = new Quiz();
        q4.question = "Insert a new record into the customers table with the values ('John', 'Doe', 'john.doe@example.com') for the columns first_name, last_name, and email, respectively.";
        q4.answer = "INSERT INTO customers (first_name, last_name, email) VALUES ('John', 'Doe', 'john.doe@example.com');";
        Quiz q5 = new Quiz();
        q5.question = "Update the salary of an employee in the employees table to 75000 where the employee_id is 101.";
        q5.answer = "UPDATE employees SET salary = 75000 WHERE employee_id = 101;";
        Quiz q6 = new Quiz();
        q6.question = "Delete rows from the orders table where the order_date is before '2023-01-01'.";
        q6.answer = "DELETE FROM orders WHERE order_date < '2023-01-01';";
        Quiz q7 = new Quiz();
        q7.question = "Count the number of orders in the orders table.";
        q7.answer = "SELECT COUNT(*) FROM orders;";
        Quiz q8 = new Quiz();
        q8.question = "Retrieve the average price of all products in the products table.";
        q8.answer = "SELECT AVG(price) FROM products;";
        Quiz q9 = new Quiz();
        q9.question = "List the product_name and total quantity ordered for each product in the order_details table, grouped by product_name.";
        q9.answer = "SELECT product_name, SUM(quantity) FROM order_details GROUP BY product_name;";
        Quiz q10 = new Quiz();
        q10.question = "Join the customers table and the orders table to retrieve the first_name and last_name of customers along with their order_id.";
        q10.answer = "SELECT customers.first_name, customers.last_name, orders.order_id FROM customers JOIN orders ON customers.customer_id = orders.customer_id;";
        arr.add(q1);
        arr.add(q2);
        arr.add(q3);
        arr.add(q4);
        arr.add(q5);
        arr.add(q6);
        arr.add(q7);
        arr.add(q8);
        arr.add(q9);
        arr.add(q10);
        AddQuizQuestions(arr);

        Quiz q;
        Scanner scanner = new Scanner(System.in);
        String check;


        for (int i = 0; i < arr.size(); i++) {
            q = getQuiz(i);
            System.out.println("For question "+i+":");
            System.out.println(q.question);
            check = scanner.next();
            if (check == q.answer) {
                System.out.println("You got the question right");
                score+=1;
            } else {
                System.out.println("You got the question wrong");
            }

        }

        System.out.println("You have finished the quiz. You score is "+score+"!");


    }
}
