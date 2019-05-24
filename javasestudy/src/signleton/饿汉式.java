package signleton;

/**
 * 线程安全
 */
public class 饿汉式 {

    private static A a = new A();

    public static A getInstance() {
        return a;
    }

}
