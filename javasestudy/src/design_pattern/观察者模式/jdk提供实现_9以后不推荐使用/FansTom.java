package design_pattern.观察者模式.jdk提供实现_9以后不推荐使用;

import java.util.Observable;
import java.util.Observer;

/**
 * 1号粉丝tom
 * 观察者，得知通知后会完成自己的业务逻辑
 */
public class FansTom implements Observer {


    @Override
    public void update(Observable o, Object arg) {
        String twitter = arg.toString();
        System.out.println("tom收到明星的推特：" + twitter);
        System.out.println("tom很开心，转发了推特");
    }
}
