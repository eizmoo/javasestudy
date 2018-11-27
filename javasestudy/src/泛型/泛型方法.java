package 泛型;

public class 泛型方法 {

    public static <K, V> boolean checkEquals(K k, V v) {
        System.out.println(k + " " + v);
        return k == v;
    }

    public static <K> void print(K k) {
        System.out.println(k.toString());
    }

    public static void main(String[] args) {
        System.out.println(checkEquals("a", "a"));
        System.out.println();
        print(new Integer(1));

    }

}
