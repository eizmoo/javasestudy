package mulit_thread.ds.source_invoke;

import mulit_thread.ds.util.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Service
public class CLInvoke implements DSInvoke {

    @Override
    public void invoke(Map<String, Object> orderInfo, ThreadLocal<Map<String, Object>> entryFormThreadLocal, CountDownLatch latch) {
        try {
            // 模拟调用第三方延时
            Thread.sleep(NumberUtils.random(50, 200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        entryFormThreadLocal.get().put("isTrue", "1");
        latch.countDown();
    }

    @Override
    public void invoke(Map<String, Object> orderInfo, ThreadLocal<Map<String, Object>> entryFormThreadLocal) {
        try {
            // 模拟调用第三方延时
            Thread.sleep(NumberUtils.random(50, 200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        entryFormThreadLocal.get().put("isTrue", "1");
    }
}
