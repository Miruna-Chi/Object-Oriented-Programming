package competition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

/**
 * Main Class<p>
 *     - opens input/output files from inline arguments<br>
 *     - instantiates a Factory to create all the different types of teams <br>
 *     - creates a Hashtable to store the teams<br>
 *     - instantiates the competition (if necessary*)<br>
 *         * creates all the observers (teams in the order they are given)<br>
 *         * starts the competition<br>
 *         * displays the winners and makes the observers(the teams) display their own rank<br>
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        File inputFile1 = new File(args[1]);
        File inputFile2;
        File outputFile = new File(args[3]);

        PrintStream o = new PrintStream(new File(String.valueOf(outputFile)));
        System.setOut(o);

        Scanner input1 = new Scanner(inputFile1);

        Hashtable<String, Team> allTeams = new Hashtable<>();
        TeamFactory factory = TeamFactory.getInstance();

        while (input1.hasNext()) {
            String [] line = input1.nextLine().split(",");

            for (int i = 0; i < line.length; i++)
                line[i] = line[i].trim();

            String teamType = line[0];
            String teamName = line[1];
            allTeams.put(teamName, factory.createTeam(teamType));

            Team team = allTeams.get(teamName);

            team.teamName = teamName;
            team.gender = line[2];
            team.numberOfPlayers = Integer.parseInt(line[3]);

            for (int i = 0; i < team.numberOfPlayers; i++) {
                Player player = new Player();
                line = input1.nextLine().split(",");
                player.name = line[0].trim();
                player.score = Integer.parseInt(line[1].trim());
                team.players.add(player);
            }

            if (args[0].equals("inscriere"))
                team.display();

        }

        if (args[0].equals("competitie")) {
            inputFile2 = new File(args[2]);
            Scanner input2 = new Scanner(inputFile2);

            String [] line = input2.nextLine().split(",");

            Competition competition = new Competition();
            competition.type = line[0].trim();
            competition.gender = line[1].trim();

            ArrayList<CurrentObservedState> teamObservers = new ArrayList<>();

            while (input2.hasNextLine()) {
                String teamName = input2.nextLine();
                Team team = allTeams.get(teamName);

                if (team.gender.equals(competition.gender) && team.teamType.equals(competition.type)) {
                    competition.teamsInOrder.add(team);
                    teamObservers.add(new CurrentObservedState(competition, team));
                }
            }

            competition.allTeamGame();
            competition.displayWinners();

            for (CurrentObservedState teamObserver : teamObservers) {
                teamObserver.displayOwnRank();
            }
            input2.close();
        }

        input1.close();
    }
}
