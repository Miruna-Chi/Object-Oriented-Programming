package competition;

/**
 * Interface needed for the Strategy design pattern implementation
 */
public interface Strategy {
    /**
     * this method will be overriden by each team's class (keeping the score in sports depends on the type of sport<br>
     *     and the gender of the team)
     * @return
     */
    public abstract double calculateScore();
}
