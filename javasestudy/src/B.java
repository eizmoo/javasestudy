
public class B extends A {

    int a;
    public B(int a) {
        super(2);
        this.a = a;
    }

    public static void main(String[] args) {
        System.out.println(new B(1).hashCode());
    }
}
