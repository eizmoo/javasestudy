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

    @Override
    public int hashCode() {
        return a;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
