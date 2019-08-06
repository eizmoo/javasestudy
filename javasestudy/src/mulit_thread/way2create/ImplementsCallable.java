package mulit_thread.way2create;

import java.util.concurrent.Callable;

public class ImplementsCallable implements Callable {
    @Override
    public Object call() throws Exception {
        System.out.println("i am running");
        return "hello world";
    }
}
