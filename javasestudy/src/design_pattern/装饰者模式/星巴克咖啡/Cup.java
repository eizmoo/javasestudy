package design_pattern.装饰者模式.星巴克咖啡;

public class Cup extends Coffee {


    public Cup() {
        description = "杯子里有 ：";
    }

    @Override
    public double cost() {
        return 0;
    }
}
