package 装饰者模式.星巴克咖啡;

public class Person {

    public static void main(String[] args) {
        Coffee coffee = new Cup();

        DarkRoastBeverage darkRoastBeverage = new DarkRoastBeverage(coffee);
        System.out.println(darkRoastBeverage.getDescription());
        MilkCondiment milkCondiment = new MilkCondiment(darkRoastBeverage);
        System.out.println(milkCondiment.getDescription());
        MilkCondiment milkCondiment1 = new MilkCondiment(milkCondiment);
        System.out.println(milkCondiment1.getDescription());
    }

}
