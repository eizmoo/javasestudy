package lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUByLinkedHashMap extends LinkedHashMap {

    /**
     * LRU中最大元素数量
     */
    private int maxSize;

    public LRUByLinkedHashMap(int maxSize) {
        // 容量为最大值/0.75，即最大负载容量为maxSize
        // accessOrder=true  根据查询排序，即最近被使用的放到后面
        super((int) Math.ceil(maxSize / 0.75) + 1, 0.75f, true);
        this.maxSize = maxSize;
    }

    /**
     * 此方法为钩子方法，map插入元素时会调用此方法
     * 此方法返回true则证明删除最老的因子
     *
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxSize;
    }
}
