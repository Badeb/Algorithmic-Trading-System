public class Main {
    public static void main(String[] args) {

        TradingSystemFacade tradingSystem = new TradingSystemFacade();
        tradingSystem.startSystem("market_data.json");

    }
}