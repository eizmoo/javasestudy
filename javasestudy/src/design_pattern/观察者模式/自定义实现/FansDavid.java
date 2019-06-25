package design_pattern.观察者模式.自定义实现;

public class FansDavid implements Observer {

    @Override
    public void update(Observer observer, Object message) {
        System.out.println("david get " + message);
    }
}
