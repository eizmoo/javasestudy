package design_pattern.装饰者模式.星巴克咖啡;

public class DarkRoastBeverage extends Beverage{
    Coffee coffee;

    public String description = "深焙咖啡";

    public DarkRoastBeverage(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + description;
    }

    @Override
    public double cost() {
        return coffee.cost() + 5.0;
    }
}
