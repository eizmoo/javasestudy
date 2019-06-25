package design_pattern.观察者模式.自定义实现;

/**
 * 观察者抽象
 */
public interface Observer {

    /**
     * 观察到事件，逻辑实现
     *
     * @param message
     * @param observer
     */
    void update(Observer observer, Object message);

}
