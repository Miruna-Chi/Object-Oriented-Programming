package competition;

/**
 * Class for all basketball teams
 */
public class BasketballTeam extends Team implements Strategy, Game {

    public BasketballTeam() {
        teamType = "basketball";
    }

    public BasketballTeam(String teamName, String gender, int numberOfPlayers) {
        super(teamName, gender, numberOfPlayers, "basketball");
    }


    /**
     * calculates Team's score based on each player's score (arithmetic mean)
     */
    @Override
    public double calculateScore() {
        double score = 0;
        for (Player player : players)
            score += player.score;

        score = (double) score/numberOfPlayers;

        return score;
    }

    /**
     * accepts a visitor (who will call a method to calculate the team's score)<p>
     * method used to implement the Visitor design pattern
     *
     * @param visitor
     * @return
     */

    @Override
    public double accept(RefereeVisitor visitor) {
        return visitor.visit(this);
    }
}
