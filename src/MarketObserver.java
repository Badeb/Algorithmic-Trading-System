import java.util.ArrayList;
import java.util.List;

interface MarketObserver {
    void update(String message);
}

class DashboardObserver implements MarketObserver {
    @Override
    public void update(String message) {
        System.out.println("-Dashboard-" + message);
    }
}

class RiskAnalysisObserver implements MarketObserver {
    @Override
    public void update(String message) {
        System.out.println("-RISK ANALYSIS- " + message);
    }
}

class MarketEventPublisher {

    private List<MarketObserver> observers = new ArrayList<>();

    public void addObserver(MarketObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(MarketObserver observer) {
        observers.remove(observer);
    }



    public void notifyObservers(String message) {
        for (MarketObserver observer : observers) {
            observer.update(message);
        }
    }
}