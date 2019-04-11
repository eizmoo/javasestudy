package guavaStudy;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

/**
 * guava测试
 */
public class GuavaMultiset {

    public static void main(String[] args) {
        /*Multiset<String> counter = HashMultiset.create();

        counter.add("a");
        counter.add("a");
        counter.add("b");
        counter.add("c", 5);
        counter.remove("c");

        System.out.println(counter.count("c"));*/

        Multimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.put("a", 1);
        multimap.put("a", 2);
        multimap.put("a", 3);

        System.out.println(multimap.get("a"));
    }

}
