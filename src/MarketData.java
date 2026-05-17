import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

interface MarketDataFormat { // Factory pattern "product" participant
    List<MarketData> read();
}

class JSONFormat implements MarketDataFormat { // "concrete product 1" participant
    private final String filePath;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public JSONFormat(String filePath){
        this.filePath = filePath;
    }

    @Override
    public List<MarketData> read() {
        List<MarketData> result = new ArrayList<>();

        //  Read the entire file line by line and combine it into a single String.
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
            return result;
        }

        //  { . } Find the blocks one by one, each one a record
        String content = sb.toString();
        int start = 0;
        while ((start = content.indexOf('{', start)) != -1) {
            int end = content.indexOf('}', start);
            if (end == -1) break;

            String block = content.substring(start + 1, end); // Example block format = "symbol":"AAPL","open":150.5
            try {
                // calling extractDouble and extractString for every value
                String symbol    = extractString(block, "symbol");
                double open      = extractDouble(block, "open");
                double high      = extractDouble(block, "high");
                double low       = extractDouble(block, "low");
                double close     = extractDouble(block, "close");
                double volume    = extractDouble(block, "volume");
                Date   timestamp = DATE_FORMAT.parse(extractString(block, "timestamp"));
                result.add(new MarketData(symbol, open, high, low, close, volume, timestamp));
            } catch (ParseException | NumberFormatException e) {
                System.err.println("Skipping malformed JSON record: " + e.getMessage());
            }

            start = end + 1;
        }

        return result;
    }


    private String extractString(String block, String key) {
        // Finds a String value by key in a JSON block.
        int keyIdx = block.indexOf("\"" + key + "\"");
        if (keyIdx == -1) return "";
        int colon = block.indexOf(':', keyIdx);               // find the ':' after the key
        int q1    = block.indexOf('"', colon + 1);   // find the opening quote of value
        int q2    = block.indexOf('"', q1 + 1);      // find the closing quote of value
        return block.substring(q1 + 1, q2);                      // return the text between quotes
    }


    private double extractDouble(String block, String key) {
        // Finds a numeric value by key in a JSON block.
        int keyIdx = block.indexOf("\"" + key + "\"");
        if (keyIdx == -1) return 0.0;
        int pos = block.indexOf(':', keyIdx) + 1;
        while (pos < block.length() && block.charAt(pos) == ' ') pos++; // if there is space skip
        int end = pos;
        while (end < block.length() &&
               (Character.isDigit(block.charAt(end)) || block.charAt(end) == '.' || block.charAt(end) == '-')) {
            end++;
        }
        return Double.parseDouble(block.substring(pos, end)); // convert String to double
    }
}

class TabularFormat implements MarketDataFormat { // "concrete product 2" participant
    private final String filePath;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public TabularFormat(String filePath){
        this.filePath = filePath;
    }

    @Override
    public List<MarketData> read() {
        List<MarketData> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // skip header
            String header = br.readLine();
            if (header == null) return result;

            // Separate each remaining line with a comma and assign them to the fields.
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(","); // seperate with ","
                if (parts.length < 7) {
                    // if colon is not complete skip
                    System.err.println("Skipping incomplete line: " + line);
                    continue;
                }

                try { // parsing right type
                    String symbol    = parts[0].trim();
                    double open      = Double.parseDouble(parts[1].trim());
                    double high      = Double.parseDouble(parts[2].trim());
                    double low       = Double.parseDouble(parts[3].trim());
                    double close     = Double.parseDouble(parts[4].trim());
                    double volume    = Double.parseDouble(parts[5].trim());
                    Date   timestamp = DATE_FORMAT.parse(parts[6].trim()); // 22.05.2025 to DATE
                    result.add(new MarketData(symbol, open, high, low, close, volume, timestamp));
                    // create object and add list

                } catch (ParseException | NumberFormatException e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
        }

        return result;
    }
}
class MarketDataFormatFactory { //  Factory pattern "creator" participant
    public static MarketDataFormat createSource(String filePath) { // declare factory method

        if (filePath.endsWith(".json")) { // add "." to json
            return new JSONFormat(filePath); // return an object of type Product (Json or Tabular)
        } else if (filePath.endsWith(".csv") || filePath.endsWith(".txt")) { // add "." to csv
            return new TabularFormat(filePath);
        } else {
            throw new RuntimeException("Unsupported file format: " + filePath);
        }
    }
}
