package competition;

/**
 * Class for all handball teams
 */
public class HandballTeam extends Team implements Strategy, Game {

    public HandballTeam() {
        teamType = "handball";
    }

    public HandballTeam(String teamName, String gender, int numberOfPlayers) {
        super(teamName, gender, numberOfPlayers, "handball");
    }

    /**
     * calculates Team's score based on gender and each player's score:<br>
     * Women's handball: product of all the players' scores<br>
     * Men's handball: sum of all the players' scores<br>
     */

    @Override
    public double calculateScore() {
        double score = 0;
        if (gender.equals("masculin"))
            for (Player player : players)
                score += player.score;
        else if (gender.equals("feminin")) {
            score = 1;
            for (Player player : players)
                score *= player.score;
        }

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
