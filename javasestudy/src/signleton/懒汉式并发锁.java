package signleton;

public class 懒汉式并发锁 {
    private static A a;

    public synchronized static A getInstance() {
        if (a == null) {
            a = new A();
        }
        return a;
    }

}
