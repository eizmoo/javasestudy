public class A {
    int a;

    public A(int a) {
        this.a = a;
    }

    public void print() {
        System.out.println(a);
    }

    @Override
    public String toString() {
        return "A{" +
                "a=" + a +
                '}';
    }
}
