import java.util.List;

abstract class AnalysisEngine {         // template class
    protected List<MarketData> data;
    public double getLastClose() {
        if (data == null || data.isEmpty()) return Double.NaN;
        return data.get(data.size() - 1).getClose();
    }
    public String getLastSymbol() {
        if (data == null || data.isEmpty()) return "";
        return data.get(data.size() - 1).getSymbol();
    }


    public void analyse(){              // template method
        loadData();
        if (data == null || data.isEmpty()) { // empty control block
            System.err.println("No data loaded, analysis aborted.");
            return;
        }

        calculate();
        generateSignal();


    }
    protected void loadData(){ // this step same all subclasses
        TradingSystemConfig config = TradingSystemConfig.getInstance();
        String filePath = config.getFilePath(); // get file path from config object

        MarketDataFormat format = MarketDataFormatFactory.createSource(filePath); // choose right read function with factory method
        data = format.read();

    }
    protected abstract void calculate();
    protected abstract void generateSignal();
    // these steps different for each class so using "abstract" keyword

}

class SMAEngine extends AnalysisEngine{
    // SMA Formula: latest N day close value average

    private int period; // number of period
    private double smaResult= Double.NaN; // prefer Nan to 0.0


    public SMAEngine ( int period){

        this.period = period;
    }

    public double getSmaResult() {
        return smaResult;
    }

    @Override
    protected void calculate() {
        if (data.size() < period) {
            System.err.println("Not enough data: need " + period + ", have " + data.size());
            return;
        }
        double sum =0;
        int start = data.size() - period;



        for(int i =start; i< data.size(); i++){
            sum += data.get(i).getClose();

            // getClose give close value the day
        }
        smaResult = sum/period; // get average

    }

    @Override
    protected void generateSignal() {
        TradingSystemConfig config = TradingSystemConfig.getInstance(); // singleton provide create only one config object
        double threshold = config.getThresholdValue(); // get threshold value from singleton object
        double lastDayClose = data.get(data.size()-1 ).getClose();

        if (Double.isNaN(smaResult)) {
            System.err.println("Calculate incomplete. Signal can not generate");
            return; // if you give wrong signal error message
        }


        if (lastDayClose > smaResult + threshold ) {
            System.out.println("SIGNAL: BUY — price is above SMA");  // trend is up buy signal

        }else if (lastDayClose < smaResult - threshold ){
            System.out.println("SIGNAL: SELL — price is below SMA"); // trend is low sell signal

        } else {
            System.out.println("SIGNAL: HOLD — price is within threshold");
        }

    }
}


class ATREngine extends AnalysisEngine{
    // ATR Formula =  ATR measures market volatility by calculating the average of True Range values over n periods.
    private int period;
    private double atrResult = Double.NaN;
    // using Nan because it can be threaded problem for example: firstly calculate method return
    // so atrResult= 0.0 so that it will create wrong generate signal

    public ATREngine(  int period){

        this.period = period;
    }

    public double getAtrResult() {
        return atrResult;
    }

    @Override
    protected void calculate() {
        if (data.size() < period || period <= 1) { // added period <= 1 control block because 0/0 is infinity problem
            System.err.println("ATR requires period > 1 ");
            return;
        }
        double trueRangeSum =0;
        int start = data.size() - period;



        for (int i = start + 1; i < data.size(); i++) {
            double high = data.get(i).getHigh();      // today high value
            double low = data.get(i).getLow();       // today low value
            double prevClose = data.get(i - 1).getClose(); // last day close value

            double trueRange = Math.max(high - low, Math.max(Math.abs(high - prevClose), Math.abs(low - prevClose)));
             //  get maximum value :
            //  current day difference, current day high - previous day closest , current day low - previous day closest
            trueRangeSum += trueRange;
        }
        atrResult = trueRangeSum / (period-1);

        }

    @Override
    protected void generateSignal() {
        TradingSystemConfig config = TradingSystemConfig.getInstance();
        double riskLimit = config.getRiskLimit(); // get risk limit to Singleton ( %5 )

        if (Double.isNaN(atrResult)) {
            System.err.println("Calculate incomplete. Signal can not generate");
            return; // if you give wrong signal error message
        }

        if (riskLimit < atrResult){
            System.out.println("SIGNAL: HIGH RISK!  market is too volatile, avoid trading");
        }else{
            System.out.println("SIGNAL: LOW RISK, market is stable safe to trade");
        }

    }
}




