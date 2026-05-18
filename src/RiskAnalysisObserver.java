public class RiskAnalysisObserver implements MarketObserver {
    @Override
    public void update(String message) {
        System.out.println("-RISK ANALYSIS- " + message);
    }
}