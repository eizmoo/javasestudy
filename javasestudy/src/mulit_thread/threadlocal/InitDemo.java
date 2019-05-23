package mulit_thread.threadlocal;

public class InitDemo {

    private ThreadLocal<Integer> resullt = ThreadLocal.withInitial(() -> {
        System.out.println("init is invoke");
        return 0;
    });

    public static void main(String[] args) {
        InitDemo initDemo = new InitDemo();
        System.out.println(initDemo.resullt.get());
        initDemo.resullt.remove();
        System.out.println(initDemo.resullt.get());
    }

}
