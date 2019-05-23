package proxy;

import java.lang.reflect.Proxy;

public class Main {

    public static void main(String[] args) {


               DynamicProxy proxy = new DynamicProxy(new Vendor());
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Sell sell = (Sell) Proxy.newProxyInstance(Sell.class.getClassLoader(), new Class[]{Sell.class}, proxy);

        sell.sell();
        sell.ad();
    }

}
