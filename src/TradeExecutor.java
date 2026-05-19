// Interface for all trade commands
interface TradeCommand {
    void execute();
}

// Buy command class
class BuyCommand implements TradeCommand {
    String symbol;
    double price;
    int quantity;

    public BuyCommand(String symbol, double price, int quantity) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    // Runs buy operation
    public void execute() {
        System.out.println("Buying " + quantity + " " + symbol + " stocks at price: " + price);
    }
}

// Sell command class
class SellCommand implements TradeCommand {
    String symbol;
    double price;
    int quantity;

    public SellCommand(String symbol, double price, int quantity) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    // Runs sell operation
    public void execute() {
        System.out.println("Selling " + quantity + " " + symbol + " stocks at price: " + price);
    }
}

// This command closes the position when risk is high
class ClosePositionCommand implements TradeCommand {
    String symbol;
    double price;

    public ClosePositionCommand(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    // Runs close position operation
    public void execute() {
        System.out.println("Closing position for " + symbol + " at price: " + price);
    }
}

// This class executes the given command
public class TradeExecutor {
    public void executeCommand(TradeCommand command) {
        command.execute();
    }
}