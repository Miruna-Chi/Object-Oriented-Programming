package competition;

/**
 * Class to hold the competition<p>
 *     - it is the Subject that all the Observers watch
 *     - has an ArrayList with the ranking of the teams, sorted by their competition score (only match points<br>
 *         count here) from highest to lowest<br>
 *     - has an ArrayList with the teams, in the order they are given
 */

import java.util.*;

public class Competition implements Subject {
    protected ArrayList<Observer> observers;
    protected String type;
    protected String gender;
    protected ArrayList<Team> teamRanking;
    protected ArrayList<Team> teamsInOrder;

    public Competition() {
        teamRanking = new ArrayList<>();
        teamsInOrder = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    /**
     * notifies all observers of the new ranking after a match has ended
     */
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(teamRanking);
        }
    }

    /**
     * All the matches in the competition are held here.<br>
     *     At the end of each match, the ranking is updated with the current scores<br>
     */
    public void allTeamGame() {

        RefereeVisitor visitor = new RefereeVisitorImpl();

        for (int i = 0; i < teamsInOrder.size(); i++) {
            for (int j = i + 1; j < teamsInOrder.size(); j++) {

                Team team1 = teamsInOrder.get(i);
                Team team2 = teamsInOrder.get(j);

                double score1 = team1.accept(visitor);
                double score2 = team2.accept(visitor);

                if (score1 > score2)
                    team1.gamePoints += 3;
                else if (score1 == score2) {
                    team1.gamePoints += 1;
                    team2.gamePoints += 1;
                } else if (score1 < score2) {
                    team2.gamePoints += 3;
                }

                teamRankUpdate(team1, team1.gamePoints, team2, team2.gamePoints);
            }
        }

    }

    /**
     * Updates ranking after each match, then notifies the observers of it
     * @param team1
     * @param score1
     * @param team2
     * @param score2
     */
    public void teamRankUpdate(Team team1, int score1, Team team2, int score2) {

        teamRanking.remove(team1);
        teamRanking.remove(team2);
        teamRanking.add(team1);
        teamRanking.add(team2);

        Comparator comparator = null;
        teamRanking.sort(comparator);
        notifyObservers();
    }

    /**
     * displays the first 3 teams (with the highest rank)
     */
    public void displayWinners() {
        int rank = 0;
        for (Team team : teamRanking) {
            rank++;

            System.out.println(team.teamName);
            if (rank == 3)
                break;

        }
    }


}
