package competition;

/**
 * Interface used in the implementation of the Observer design pattern
 */
public interface Subject {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers();
}
