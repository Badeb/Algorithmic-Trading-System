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
        AnalysisEngine smaEngine = new SMAEngine(3);
        smaEngine.analyse();

        System.out.println("\n--- ATR Risk Analysis ---");
        AnalysisEngine atrEngine = new ATREngine(3);
        atrEngine.analyse();

        System.out.println("\n=== System Finished ===");
    }
}