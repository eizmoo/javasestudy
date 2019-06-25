package design_pattern.观察者模式.自定义实现;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 被观察者,Observer要注意重新hashCode和equals
 */
public class Obserable {

    private CopyOnWriteArrayList<Observer> observers;

    public Obserable() {
        this.observers = new CopyOnWriteArrayList<>();
    }

    protected void notice(Object message) {
        observers.forEach(observer -> {
            observer.update(observer, message);
        });
    }

    protected void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    protected void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
