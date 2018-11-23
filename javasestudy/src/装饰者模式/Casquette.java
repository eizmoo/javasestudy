package 装饰者模式;

public class Casquette extends HatDecorator {

    Person person;

    public Casquette(Person person) {
        this.person = person;
    }

    @Override
    public String getDescription() {
        return person.getDescription() + " casquette * 1";
    }

    @Override
    public double cost() {
        return 5.8 + person.cost();
    }
}
