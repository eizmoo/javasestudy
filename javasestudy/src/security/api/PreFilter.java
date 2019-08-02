package security.api;

import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PreFilter {

    public static void doFilter() {
        // 1. 时间戳
        // 2. sign 根据参数data的加密sign
        // 3. 商家code

        // body 结构
        // {
        //      "timestamp":"",
        //      "sign":"",
        //      "name":""
        //       ...
        // }

        Map<String, Object> param = paramBuilder();

        // 必填参数校验
        if (!checkNecessaryParam(param)) {
            System.err.println("验签失败，缺少必要参数");
        }

        // 时间戳校验，允许时间在当前时间的前后30s内
        long timestamp = (long) param.get("timestamp");

        if (!checkTimestamp(timestamp)) {
            System.err.println("请求时间戳有误");
        }

        // 签名加密校验，此处加密值可加盐
        StringBuilder builder = new StringBuilder();
        Map<String, Object> sortParam = new TreeMap<>(param);
        sortParam.remove("sign");
        sortParam.forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));

        String sign = builder.toString();

        sign = DigestUtils.md5DigestAsHex(sign.substring(0, sign.length() - 1).getBytes());

        if (!param.containsKey("sign") || !param.get("sign").equals(sign)) {
            System.err.println("验签失败，签名错误");
        }
    }

    private static boolean checkTimestamp(long timestamp) {
        long nowTimestamp = System.currentTimeMillis();
        // 此值来自数据库，默认30s
        long available = 30 * 1000;
        // 时间在当前时间的前后30s区间内
        return timestamp > nowTimestamp - available && timestamp < nowTimestamp + available;
    }

    /**
     * 是否包含必要参数
     *
     * @param param
     * @return
     */
    private static boolean checkNecessaryParam(Map<String, Object> param) {
        return param.containsKey("sign") && param.containsKey("merchantCode") && param.containsKey("timestamp");
    }

    public static Map<String, Object> paramBuilder() {
        Map<String, Object> param = new HashMap<>();
        param.put("merchantCode", "abc123");
        param.put("timestamp", System.currentTimeMillis());
        param.put("sign", "e4ad5a8319c04a8f469d99b6ec1f7254");
        param.put("name", "david");
        param.put("idno", "372928199706020217");
        param.put("phone", "13812345678");

        return param;
    }

    public static void main(String[] args) {
        doFilter();
    }

}
