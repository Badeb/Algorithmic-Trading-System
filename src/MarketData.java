import java.util.Date;
import java.util.List;

public class MarketData {
    private String symbol;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private Date timestamp;


    public MarketData(String symbol, double open, double high, double low, double close, double volume, Date timestamp) {
        this.symbol = symbol;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

interface MarketDataFormat {

    List<MarketData> read();

}

class JSONFormat implements MarketDataFormat {
    private String filePath;

    public JSONFormat(String filePath){
        this.filePath = filePath;
    }

    @Override
    public List<MarketData> read() {
        return List.of();
    }
}
class TabularFormat implements MarketDataFormat {
    private String filePath;

    public TabularFormat(String filePath){
        this.filePath = filePath;
    }

    @Override
    public List<MarketData> read() {
        return List.of();
    }
}

class MarketDataFormatFactory {


    public static MarketDataFormat createSource(String filePath ) {
        if (filePath.endsWith("json")) {
            return new JSONFormat(filePath);
        }
        else if(filePath.endsWith("csv") || filePath.endsWith(".txt")) {
            return new TabularFormat(filePath);
        }
        else {
            throw new RuntimeException();
        }

    }


}