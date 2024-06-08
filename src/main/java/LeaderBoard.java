public class LeaderBoard {
    QuizApplication.Difficulty d;
    String name; // name of player
    int score; // score of player
    LeaderBoard(QuizApplication.Difficulty d, String name, int score) {
        this.d = d;
        this.name = name;
        this.score = score;
    }
}
