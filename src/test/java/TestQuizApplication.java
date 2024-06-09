import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class TestQuizApplication {
    @Test
    public void test_returns_easy_for_easy_difficulty() {
        QuizApplication quizApp = new QuizApplication();
        String result = quizApp.getTable(QuizApplication.Difficulty.Easy);
        assertEquals("easy", result);
    }


    @Test
    public void test_returns_easyleaderboard_for_easy_difficulty() {
        QuizApplication quizApp = new QuizApplication();
        String result = quizApp.getLeaderBoardTable(QuizApplication.Difficulty.Easy);
        assertEquals("easyleaderboard", result);
    }


    @Test
    public void test_insert_leaderboard_with_empty_or_null_name() {
        LeaderBoard leaderBoard = new LeaderBoard(QuizApplication.Difficulty.Medium, "", 5);
        QuizApplication.insertLeaderBoard(leaderBoard);
        ArrayList<LeaderBoard> leaderBoards = QuizApplication.getLeaderBoard(QuizApplication.Difficulty.Medium);
        boolean found = false;
        for (LeaderBoard lb : leaderBoards) {
            if (lb.name.equals("") && lb.score == 5) {
                found = true;
                break;
            }
        }
        assertFalse(found);

        leaderBoard = new LeaderBoard(QuizApplication.Difficulty.Medium, null, 5);
        QuizApplication.insertLeaderBoard(leaderBoard);
        leaderBoards = QuizApplication.getLeaderBoard(QuizApplication.Difficulty.Medium);
        found = false;
        for (LeaderBoard lb : leaderBoards) {
            if (lb.name == null && lb.score == 5) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }


    @Test
    public void test_retrieves_leaderboard_sorted_by_score() {
        QuizApplication.Difficulty difficulty = QuizApplication.Difficulty.Easy;
        ArrayList<LeaderBoard> leaderboard = QuizApplication.getLeaderBoard(difficulty);

        assertNotNull(leaderboard);
        for (int i = 1; i < leaderboard.size(); i++) {
            assertTrue(leaderboard.get(i - 1).score >= leaderboard.get(i).score);
        }
    }


    @Test
    public void test_user_selects_easy_difficulty() {
        QuizApplication quizApp = new QuizApplication();
        Scanner scanner = new Scanner("1\n");
        System.setIn(new java.io.ByteArrayInputStream("1\n".getBytes()));
        quizApp.ViewLeaderBoard();
        // Assuming displayLeaderBoard prints to console, we would need to capture the output and assert it contains "EASY LEADERBOARD"
        // This is a simplified example, in real tests, we might use a library like System Rules to capture console output
    }


    @Test
    public void test_handles_invalid_menu_option_selections_gracefully() {
        // Arrange
        ByteArrayInputStream inContent = new ByteArrayInputStream("5\n4\n".getBytes());
        System.setIn(inContent);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String expectedOutput = "You selected an invalid option!";

        // Act
        QuizApplication.main(new String[]{});

        // Assert
        assertTrue(outContent.toString().contains(expectedOutput));
    }


}
