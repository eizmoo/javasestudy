package 泛型;

public class Main {

    public static void main(String[] args) {
        GenericClass<String> str = new GenericClass<>("1");
        GenericClass<Integer> integer = new GenericClass<>(1);

        System.out.println(str.getClass() == integer.getClass());
    }




}
