package mulit_thread.ds.util;

public class NumberUtils {

    public static int random(int start, int end) {
        int diff = end - start;
        return (int) (Math.random() * diff + start);
    }

}
