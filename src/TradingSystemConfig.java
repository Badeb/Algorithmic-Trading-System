public class TradingSystemConfig {

    private double riskLimit; //How many % should the pricr drop to trigger a sell order
    private double thresholdValue; // How far above the SMA the current price must be to trigger a BUY signal
    private String strategyType; //Short term or Long

    private String filePath;
    private String outputDirectory;

    private static TradingSystemConfig instance = null;

    private TradingSystemConfig() {
        this.riskLimit = 0.05;
        this.thresholdValue = 0.0;
        this.strategyType = "Short-Term";
        this.filePath = "data/market_data.json";
        this.outputDirectory = "output/";

    }
    public static TradingSystemConfig getInstance() {
        if(instance == null){
            instance = new TradingSystemConfig();
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

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }


}
