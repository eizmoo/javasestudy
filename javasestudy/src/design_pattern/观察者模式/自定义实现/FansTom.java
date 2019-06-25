package design_pattern.观察者模式.自定义实现;


/**
 * 观察者tom
 */
public class FansTom implements Observer{
    @Override
    public void update(Observer observer, Object message) {
        System.out.println("tom get " + message);
    }
}
