import java.util.ArrayList;
import java.util.List;

public class MarketEventPublisher {

    private List<MarketObserver> observers = new ArrayList<>();

    public void addObserver(MarketObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (MarketObserver observer : observers) {
            observer.update(message);
        }
    }
}