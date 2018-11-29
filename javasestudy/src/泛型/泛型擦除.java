package 泛型;

public class 泛型擦除 {

    /**
     * Java中的泛型不是语言内在的机制，而是后来添加的特性，
     * 这样就带来一个问题：非泛型代码和泛型代码的兼容性。
     * 泛型是JDK1.5才添加到Java中的，那么之前的代码全部都是非泛型的，它们如何运行在JDK1.5及以后的VM上？
     * 为了实现这种兼容性，Java泛型被局限在一个很狭窄的地方，
     * 为了实现与非泛型代码的兼容，Java语言的泛型采用擦除(Erasure)来实现
     * 也就是泛型基本上由编译器来实现，由编译器执行类型检查和类型推断，然后在生成字节码之前将其清除掉
     * @param args
     */

    public static void main(String[] args) {
        泛型类 exampleInteger = new 泛型类<Integer>(123);
        泛型类 exampleString = new 泛型类<String>("123");

        System.out.println(exampleInteger.getClass() == exampleString.getClass());

    }
}
