package competition;

/**
 * Here the visitor/referee checks the score of each team by using the Strategy design pattern<br>
 *     - Strategy is designed to call the "calculateScore" method of each team, without having to manually<br>
 *         call them and distinguish between types of score keeping in different sports
 */
public class RefereeVisitorImpl implements RefereeVisitor{

    public double visit(BasketballTeam basketballTeam) {
        StrategyContext context = new StrategyContext(basketballTeam);
        context.executeStrategy();
        return context.executeStrategy();
    }

    public double visit(FootballTeam footballTeam) {
        StrategyContext context = new StrategyContext(footballTeam);
        return context.executeStrategy();
    }

    public double visit(HandballTeam handballTeam) {
        StrategyContext context = new StrategyContext(handballTeam);
        return context.executeStrategy();
    }

}
