package design_pattern.观察者模式.jdk提供实现_9以后不推荐使用;

import java.util.Observable;
import java.util.Observer;

/**
 * 2号粉丝david
 */
public class FansDavid implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        String twitter = arg.toString();
        System.out.println("david收到明星的推特：" + twitter);
        System.out.println("明星在推特中公布恋情，david很失落");
    }
}
