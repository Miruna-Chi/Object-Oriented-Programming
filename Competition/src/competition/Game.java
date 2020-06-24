package competition;

/**
 * Interface for the Visitor design pattern:<br>
 *     The referee will visit every match between the teams to check the score and determine who wins<br>
 */
public interface Game {
    public double accept (RefereeVisitor visitor);
}
