package 装饰者模式.星巴克咖啡;

public class DarkRoastBeverage extends Beverage{
    Cup cup;

    public String description = "深焙咖啡";

    public DarkRoastBeverage(Cup cup) {
        this.cup = cup;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + description;
    }

    @Override
    public double cost() {
        return cup.cost() + 5.0;
    }
}
