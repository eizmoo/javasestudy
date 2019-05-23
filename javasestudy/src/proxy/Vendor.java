package proxy;

public class Vendor implements Sell {
    @Override
    public void sell() {
        System.out.println("i am sell");
    }

    @Override
    public void ad() {
        System.out.println("i am ad");
    }
}
