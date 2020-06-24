package competition;

/**
 * Class needed for the Strategy design pattern implementation
 */
public class StrategyContext {
    private Strategy strategy;

    public StrategyContext(Strategy strategy){
        this.strategy = strategy;
    }

    /**
     * method that will be called by several different classes, each with their own implementation
     */
    public double executeStrategy(){
        return strategy.calculateScore();
    }
}
