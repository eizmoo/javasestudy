package classloader;

import guavaStudy.GuavaMultiset;

/**
 * 打印类加载器类信息
 */
public class BaseClassloaderPrint {
    public static void main(String[] args) {
        ClassLoader loader = GuavaMultiset.class.getClassLoader();

        while (loader != null) {
            System.out.println(loader);
            loader = loader.getParent();
        }
    }
}
