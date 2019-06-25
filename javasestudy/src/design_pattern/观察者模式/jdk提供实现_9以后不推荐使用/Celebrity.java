package design_pattern.观察者模式.jdk提供实现_9以后不推荐使用;

import java.util.Observable;

/**
 * 明星，被观察者对象
 */
public class Celebrity extends Observable {

    /**
     * 发送推特
     *
     * @param twitter
     */
    public void publishTwitter(String twitter) {
        System.out.println("明星发送新推特，内容：" + twitter);
        // 设置发生改变
        this.setChanged();
        // 通知观察者
        this.notifyObservers(twitter);
    }

}
