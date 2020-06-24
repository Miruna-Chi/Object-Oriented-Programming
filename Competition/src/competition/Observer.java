package competition;


import java.util.ArrayList;

/**
 * Interface for the Observer design pattern:<br>
 * - will be implemented to update each team with the ranking after each match
 */

public interface Observer {
    public void update(ArrayList<Team> teamRanking);
}
