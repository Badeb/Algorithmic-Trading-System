public class TradingSystemConfig {

    private double riskLimit; //How many % should the pricr drop to trigger a sell order
    private double thresholdValue; // How far above the SMA the current price must be to trigger a BUY signal
    private String strategyType; //Short term or Long
    private String filePath; // It shows where to read the data from.
    private static volatile TradingSystemConfig instance = null;
    // using volatile keyword because of if there are two thread at the same time.

    private TradingSystemConfig() {
        this.riskLimit = 0.05;
        this.thresholdValue = 0.0;
        this.strategyType = "Short-Term";
        this.filePath = "data/market_data.json";


    }
    public static TradingSystemConfig getInstance() {
        if (instance == null) {  // First control
            synchronized (TradingSystemConfig.class) { // get lock
                if (instance == null) { // second control
                    instance = new TradingSystemConfig();
                }
            }

            // `volatile` alone does not provide atomic object creation.
            // `synchronize` alone will lock every call, slowing things down.
            // Both work correctly and efficiently together.
        }
        return instance;
    }

    public double getRiskLimit() {
        return riskLimit;
    }

    public void setRiskLimit(double riskLimit) {
        this.riskLimit = riskLimit;
    }

    public double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

 }
