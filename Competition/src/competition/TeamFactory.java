package competition;

/**
 * Factory class: creates teams, based on their types and assigns them to the right classes<p>
 * Singleton implementation: TeamFactory has a unique instance (we don't need more than one factory to create and<br>
 * assign objects
 */
public class TeamFactory {
    private static TeamFactory uniqueInstance;

    private TeamFactory () {

    }

    public static TeamFactory getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new TeamFactory();
        }
        return uniqueInstance;
    }

    public Team createTeam (String teamType) {
        Team team = null;

        if (teamType.equals("basketball"))
            team = new BasketballTeam();
        else if (teamType.equals("football"))
            team = new FootballTeam();
        else if (teamType.equals("handball"))
            team = new HandballTeam();

        return team;
    }

}
