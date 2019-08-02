package mulit_thread.ds.source_invoke;

import mulit_thread.ds.util.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PPXInvoke implements DSInvoke {
    @Override
    public void invoke(Map<String, Object> orderInfo, ThreadLocal<Map<String, Object>> entryFormThreadLocal) {
        try {
            // 模拟调用第三方延时
            Thread.sleep(NumberUtils.random(50, 200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        entryFormThreadLocal.get().put("isBlack", "1");
    }
}
