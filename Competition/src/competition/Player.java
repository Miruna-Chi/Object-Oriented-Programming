package competition;

/**
 * Class for storing each player's attributes
 */
public class Player {
    protected String name;
    protected int score;

    public Player () {

    }

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
