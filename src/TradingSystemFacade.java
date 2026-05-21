// Facade class for the whole trading system
// It hides configuration, analysis, observer and command details from Main
public class TradingSystemFacade {

    public void startSystem() {
        System.out.println("=== Algorithmic Trading System Started ===");

        setupConfiguration();

        SMAEngine smaEngine = runAnalysis();

        sendNotifications();

        executeTradeCommands(smaEngine);

        System.out.println("\n=== System Finished ===");
    }

    // Sets global system settings with Singleton config
    private void setupConfiguration() {
        TradingSystemConfig config = TradingSystemConfig.getInstance();

        config.setFilePath("market_data.json");
        config.setRiskLimit(5.0);
        config.setThresholdValue(0.0);
        config.setStrategyType("Short-Term");

        System.out.println("File Path: " + config.getFilePath());
        System.out.println("Risk Limit: " + config.getRiskLimit());
        System.out.println("Strategy: " + config.getStrategyType());
    }

    // Runs SMA and ATR analysis engines
    // SMAEngine is returned because trade commands use its results
    private SMAEngine runAnalysis() {
        System.out.println("\n--- SMA Analysis ---");

        SMAEngine smaEngine = new SMAEngine(3);
        smaEngine.analyse();

        System.out.println("\n--- ATR Risk Analysis ---");

        ATREngine atrEngine = new ATREngine(3);
        atrEngine.analyse();

        return smaEngine;
    }

    // Sends messages to dashboard and risk observers
    private void sendNotifications() {
        System.out.println("\n--- Observer Notifications ---");

        MarketEventPublisher publisher = new MarketEventPublisher();

        publisher.addObserver(new DashboardObserver());
        publisher.addObserver(new RiskAnalysisObserver());

        publisher.notifyObservers("SMA signal generated.");
        publisher.notifyObservers("ATR risk analysis completed.");
    }

    // Runs trade commands using SMAEngine results
    private void executeTradeCommands(SMAEngine smaEngine) {
        System.out.println("\n--- Command Pattern Trade Execution ---");

        String symbol = smaEngine.getLastSymbol();
        double currentPrice = smaEngine.getLastClose();
        double sma = smaEngine.getSmaResult();
        double entryPrice = smaEngine.getFirstClose();
        int quantity = 10;

        TradingSystemConfig config = TradingSystemConfig.getInstance();
        TradeExecutor tradeExecutor = new TradeExecutor();

        System.out.println("Symbol: " + symbol);
        System.out.println("Current Price: " + currentPrice);
        System.out.println("SMA Value: " + sma);

        // If current price is higher than SMA, system gives buy signal
        if (currentPrice > sma) {
            System.out.println("Decision: Current price is above SMA, so BUY command will run.");
            TradeCommand buyCommand = new BuyCommand(symbol, currentPrice, quantity);
            tradeExecutor.executeCommand(buyCommand);
        }
        // Otherwise, system gives sell signal
        else {
            System.out.println("Decision: Current price is below or equal to SMA, so SELL command will run.");
            TradeCommand sellCommand = new SellCommand(symbol, currentPrice, quantity);
            tradeExecutor.executeCommand(sellCommand);
        }

        // Risk control using previous close as entry price
        double lossRate = ((entryPrice - currentPrice) / entryPrice) * 100;

        System.out.println("Entry Price: " + entryPrice);

        if (lossRate > 0) {
            System.out.println("Loss Rate: " + lossRate + "%");
        } else {
            System.out.println("Profit Rate: " + (-lossRate) + "%");
        }

        System.out.println("Risk Limit: " + config.getRiskLimit() + "%");

        // If loss rate is higher than risk limit, position is closed
        if (lossRate > config.getRiskLimit()) {
            System.out.println("Risk decision: Loss rate is higher than risk limit.");
            TradeCommand closeCommand = new ClosePositionCommand(symbol, currentPrice);
            tradeExecutor.executeCommand(closeCommand);
        } else {
            System.out.println("Risk decision: Risk limit is not exceeded, position stays open.");
        }
    }
}