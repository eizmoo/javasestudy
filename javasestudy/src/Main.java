
import java.util.*;

public class Main {


    public static void main(String[] args) {
        String a = "1";
        Map<String, Object> map = new HashMap<>();
        map.put(a, "1");

        a = "2";

        map.keySet().forEach(System.out::println);

        Map<A, Object> map1 = new HashMap<>();
        A a1 = new A(1);

        map1.put(a1, 1);
        a1 = new A(2);

        map1.keySet().forEach(System.out::println);
    }
}
