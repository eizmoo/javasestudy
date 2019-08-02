package mulit_thread.ds;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mulit_thread.ds.source_invoke.DSInvoke;
import mulit_thread.ds.source_invoke.PPXInvoke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 执行
 */
@ComponentScan
public class Main {

    @Autowired
    static Map<String, DSInvoke> dsMap = new HashMap<>();

    @Autowired
    static PPXInvoke ppxInvoke;

    private static ThreadLocal<Map<String, Object>> entryFormThreadLocal = ThreadLocal.withInitial(Maps::newConcurrentMap);

    private List<String> invokeDS = Lists.newArrayList("clInvoke", "ppxInvoke");

    private Map<String, Object> map = Maps.newConcurrentMap();

    public static void main(String[] args) {
        int time = 10;

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            new Main().invoke();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("invoke: " + (endTime - startTime) / time);

        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            new Main().invokeMulti();
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("invokeMulti: " + (endTime1 - startTime1 / time));
    }

    public void invokeMulti() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(10);
        pool.setMaxPoolSize(200);
        pool.setKeepAliveSeconds(60);
        pool.setQueueCapacity(500);
        pool.setThreadNamePrefix("ds_job_");

        CountDownLatch latch = new CountDownLatch(invokeDS.size());

        invokeDS.forEach(ds -> {
            DSInvoke dsInvoke = dsMap.get(ds);
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "正在执行");
                dsInvoke.invoke(map, entryFormThreadLocal, latch);
            });
        });
        try {
            System.out.println(Thread.currentThread().getName() + "线程阻塞~~");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "继续执行");
    }

    public void invoke() {
        invokeDS.forEach(ds -> dsMap.get(ds).invoke(map, entryFormThreadLocal));
    }


}
