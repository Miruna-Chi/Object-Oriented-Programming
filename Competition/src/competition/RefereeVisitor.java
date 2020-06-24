package competition;

/**
 * There are 3 types of sports, so there will be 3 types of referees/visitors
 */
public interface RefereeVisitor {
    double visit(BasketballTeam team);
    double visit(FootballTeam team);
    double visit(HandballTeam team);
}
