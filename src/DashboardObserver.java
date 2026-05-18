public class DashboardObserver implements MarketObserver {
    @Override
    public void update(String message){
        System.out.println("-Dashboard-"+ message);
    }



}
