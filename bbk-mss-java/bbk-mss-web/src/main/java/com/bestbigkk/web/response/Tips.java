package com.bestbigkk.web.response;

/**
 * @author: 开
 * @date: 2020-03-23 20:10:46
 * @describe: 补充信息实体，可以在响应数据的时候，主动向其中设置额外信息，响应包装类会自动作为msg信息返回。
 */
public class Tips {
    private static final ThreadLocal<String> MSG = new ThreadLocal<>();

    public static String getAndRemove() {
        try {
            return MSG.get();
        }finally {
            MSG.remove();
        }
    }

    public static void set(String content) {
        MSG.set(content);
    }

}
