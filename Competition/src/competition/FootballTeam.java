package competition;

/**
 * Class for all football teams
 */
public class FootballTeam extends Team implements Strategy, Game {

    public FootballTeam() {
        teamType = "football";
    }

    public FootballTeam(String teamName, String gender, int numberOfPlayers) {
        super(teamName, gender, numberOfPlayers, "football");
    }

    /**
     * calculates Team's score based on gender and each player's score:<br>
     * Women's football: 2 * score of the worst player + sum of the other players' scores<br>
     * Men's football: 2 * score of the best player + sum of the other players' scores<br>
     */
    @Override
    public double calculateScore() {
        double score = 0;
        int minScore = players.get(0).score;
        int maxScore = players.get(0).score;

        for (Player player : players) {
            if (minScore > player.score)
                minScore = player.score;
            if (maxScore < player.score)
                maxScore = player.score;
        }

        for (Player player : players)
            score += player.score;

        if (gender.equals("masculin"))
            score += maxScore;
        else if (gender.equals("feminin"))
            score += minScore;

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
