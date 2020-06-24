package competition;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Class for all teams, no matter the type
 */
public class Team implements Comparable<Team> {
    protected String teamName;
    protected String gender;
    protected int numberOfPlayers;
    protected ArrayList<Player> players;
    protected String teamType;
    protected int gamePoints; //points gained in competition

    public Team () {
        players = new ArrayList<>();
        teamType = "unknown type";
    }

    public Team(String teamName, String gender, int numberOfPlayers, String teamType) {
        this.teamName = teamName;
        this.gender = gender;
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
        this.teamType = teamType;
    }

    /**
     * displays all teams, attributes of the teams and team members
     */
    public void display() {
        System.out.print("{teamName: " + teamName + ", gender: " + gender + ", numberOfPlayers: " + numberOfPlayers
        + ", players: [");

        ListIterator<Player> playerIterator = players.listIterator();

        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            System.out.print("{name: " + player.name + ", score: " + player.score + "}");

            if (playerIterator.hasNext())
                System.out.print(", ");
        }

        System.out.println("]}");
    }

    public double accept(RefereeVisitor visitor) {
        return 0;
    }

    /**
     * sorts the teams by their gained points in matches (needed for ranking)
     * @param t2
     * @return
     */
    @Override
    public int compareTo(Team t2) {


        if (this.gamePoints > t2.gamePoints)
            return -1;
        else if (this.gamePoints == t2.gamePoints)
            return 0;
        else
            return 1;
    }
}
