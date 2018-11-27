package 泛型;

public class Main {

    public static void main(String[] args) {
        泛型类<String> str = new 泛型类<>("1");
        泛型类<Integer> integer = new 泛型类<>(1);

        System.out.println(str.getClass() == integer.getClass());
    }




}
