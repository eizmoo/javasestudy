package basicdatatype.cache;

public class IntegerCacheTest {

    public static void main(String[] args) {

        /**
         * IntegerCache仅对Integer类型的自动装箱后的对象有效，因为缓存是在valueOf方法中产生的
         */

        Integer a = 10, b = 10;
        System.out.println("a == b? " + (a == b));
        Integer c = 1000, d = 1000;
        System.out.println("c == d? " + (c == d));

        Integer a1 = new Integer(10);
        Integer b1 = new Integer(10);
        System.out.println("a1 == b1? " + (a1 == b1));
    }

}
