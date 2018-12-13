package 泛型;

public class GenericClass<T> {

    private T var;

    public GenericClass(T var) {
        this.var = var;
    }

    public GenericClass() {
    }

    public T getVar() {
        return var;
    }

    public void setVar(T var) {
        this.var = var;
    }

    public static void main(String[] args) {

    }
}
