package design_pattern.观察者模式.自定义实现;

public class Celebrity extends Obserable {

    public void publishTwitter(String twitter) {
        this.notice(twitter);
    }

}
