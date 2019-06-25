package design_pattern.观察者模式.jdk提供实现_9以后不推荐使用;

/**
 * 客户端
 */
public class Client {
    public static void main(String[] args) {
        Celebrity celebrity = new Celebrity();
        celebrity.addObserver(new FansTom());
        celebrity.addObserver(new FansDavid());

        celebrity.publishTwitter("我和Angle在一起啦~");
    }
}
