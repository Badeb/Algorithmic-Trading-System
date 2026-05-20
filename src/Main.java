    public class Main {
    public static void main(String[] args) {

        System.out.println("=== Algorithmic Trading System Started ===");

        // Singleton: global ayarlar
        TradingSystemConfig config = TradingSystemConfig.getInstance();

        // File path
        config.setFilePath("market_data.json");

        // Risk limit
        config.setRiskLimit(5.0);

        // SMA threshold
        config.setThresholdValue(0.0);

        // Strategy Type
        config.setStrategyType("Short-Term");

        System.out.println("File Path: " + config.getFilePath());
        System.out.println("Risk Limit: " + config.getRiskLimit());
        System.out.println("Strategy: " + config.getStrategyType());

        System.out.println("\n--- SMA Analysis ---");
        SMAEngine smaEngine = new SMAEngine(3);
        smaEngine.analyse();


        System.out.println("\n--- ATR Risk Analysis ---");
        ATREngine atrEngine = new ATREngine(3);
        atrEngine.analyse();

        System.out.println("\n--- Observer Notifications ---");

        MarketEventPublisher publisher = new MarketEventPublisher();

        publisher.addObserver(new DashboardObserver());
        publisher.addObserver(new RiskAnalysisObserver());

        publisher.notifyObservers("SMA signal generated.");
        publisher.notifyObservers("ATR risk analysis completed.");

        System.out.println("\n--- Command Pattern Trade Execution ---");

     // Example values for trade execution
        String symbol       = smaEngine.getLastSymbol();
        double currentPrice = smaEngine.getLastClose();
        double sma          = smaEngine.getSmaResult();
        int quantity = 10;

       // TradeExecutor runs the given command
        TradeExecutor tradeExecutor = new TradeExecutor();

     // If current price is higher than SMA, system gives buy signal
        if (currentPrice > sma) {
            TradeCommand buyCommand = new BuyCommand(symbol, currentPrice, quantity);
            tradeExecutor.executeCommand(buyCommand);
        }
     // Otherwise, system gives sell signal
        else {
            TradeCommand sellCommand = new SellCommand(symbol, currentPrice, quantity);
            tradeExecutor.executeCommand(sellCommand);
        }

        // Risk control example
        double buyPrice = 200.0;
        double lossRate = ((buyPrice - currentPrice) / buyPrice) * 100;
        // If loss rate is higher than risk limit, position is closed
        if (lossRate > config.getRiskLimit()) {
            TradeCommand closeCommand = new ClosePositionCommand(symbol, currentPrice);
            tradeExecutor.executeCommand(closeCommand);
        }



        System.out.println("\n=== System Finished ===");
    }
}