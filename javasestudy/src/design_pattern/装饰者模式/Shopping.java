package design_pattern.装饰者模式;

public class Shopping {

    public static void main(String[] args) {
        Person person = new Tennager();

        person = new Shirt(person);

        System.out.println(person.getDescription() + "\ncost:" + person.cost());

        person = new Casquette(person);

        System.out.println(person.getDescription() + "\ncost:" + person.cost());
    }

}
