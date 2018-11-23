package 装饰者模式;

public class Shirt extends ClothingDecorator {

    Person person;

    public Shirt(Person person) {
        this.person = person;
    }

    @Override
    public String getDescription() {
        return person.getDescription() + "shirt * 1";
    }

    @Override
    public double cost() {
        return 18.9 + person.cost();
    }

}
