package design_pattern.观察者模式.自定义实现;

public class Client {
    public static void main(String[] args) {
        Celebrity celebrity = new Celebrity();

        celebrity.addObserver(new FansTom());
        celebrity.addObserver(new FansDavid());

        celebrity.publishTwitter(" ahh");
    }
}
