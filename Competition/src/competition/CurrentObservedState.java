package competition;

import java.util.ArrayList;

/**
 * Class that helps with the Observer design pattern implementation:<br>
 *     - the observers get notified each time a match ends and they are given the current ranking of all the teams<br>
 *     - at the end of the competition (when all the games between all the teams have ended), they will display<br>
 *         their own rank
 */
public class CurrentObservedState implements Observer {
    ArrayList<Team> ranking;
    Team team;
    Subject competition;

    public CurrentObservedState(Subject competition, Team team){
        this.competition = competition;
        this.team = team;
        competition.registerObserver(this);
        ranking = new ArrayList<>();
    }

    public void update(ArrayList<Team> ranking) {
        this.ranking = ranking;
    }

    public void displayOwnRank() {
        int rank = 0;
        for (Team team : ranking) {
            rank++;

            if (team == this.team) {
                System.out.println("Echipa " + team.teamName + " a ocupat locul " + rank);
                break;
            }


        }
    }
}
