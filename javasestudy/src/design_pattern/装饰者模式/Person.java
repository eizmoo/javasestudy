package design_pattern.装饰者模式;

/**
 * 基类,约束整个继承树的行为
 */
public abstract class Person {
    String description = "unkonwn";

    public String getDescription() {
        return description;
    }

    public abstract double cost();


}
