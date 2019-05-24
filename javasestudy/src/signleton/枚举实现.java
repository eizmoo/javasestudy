package signleton;

/**
 * 线程安全
 */
public enum 枚举实现 {
    INSTANCE;

    private A a;

    枚举实现() {
        this.a = new A();
    }

    public A getInstance() {
        return a;
    }
    /**
     * 用法
     * 枚举实现.INSTANCE.getInstance();
     */
}
