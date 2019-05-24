package signleton;

/**
 * 线程安全
 */
public class 双重检查锁 {

    private static volatile A a;

    public static A getInstance() {
        if (a == null) {
            synchronized (双重检查锁.class) {
                if (a == null) {
                    a = new A();
                }
            }
        }
        return a;
    }

}
