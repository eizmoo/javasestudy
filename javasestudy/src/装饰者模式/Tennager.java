package 装饰者模式;

/**
 * 被装饰者 Tennager
 */
public class Tennager extends Person{
    public Tennager() {
        description = "shopping list:";
    }

    @Override
    public double cost() {
        return 0;
    }
}
