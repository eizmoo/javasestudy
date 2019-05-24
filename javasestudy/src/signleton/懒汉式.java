package signleton;


/**
 * 线程不安全
 */
public class 懒汉式 {

    private static A a;

    public static A getInstance() {
        if (a == null) {
            a = new A();
        }
        return a;
    }
}
