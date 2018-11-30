package 装饰者模式.星巴克咖啡;

public class MilkCondiment extends Condiment {
    private Coffee coffee;

    public MilkCondiment(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription()+" 牛奶 × 1";
    }

    @Override
    public double cost() {
        return coffee.cost() + 1.0;
    }

}
