package 装饰者模式.星巴克咖啡;

public abstract class Cup {

    public String description = "unknown";


    public String getDescription() {
        return description;
    }

    public abstract double cost();



}
