package 泛型;

import 继承树.Daughter;
import 继承树.Father;
import 继承树.Son;

import java.util.ArrayList;
import java.util.List;

public class 泛型通配符 {

    private static <K extends Number> void print(K k) {
        System.out.println(k + " " + k.getClass());
    }


    public static void main(String[] args) {
        print(new Integer(1));
        print(Double.valueOf(1.0));

        List<? extends Father> extendsList = new ArrayList<>();
        List<? super Father> superList = new ArrayList<>();

        superList.add(new Father());
        superList.add(new Son());
        superList.add(new Daughter());

        /*extendsList.add(new Father());
        extendsList.add(new Son());
        extendsList.add(new Son());*/

    }


}
