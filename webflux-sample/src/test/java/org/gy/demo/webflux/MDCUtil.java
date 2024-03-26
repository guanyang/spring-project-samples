package org.gy.demo.webflux;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.MDC;

public class MDCUtil {

    public static void main(String[] args) {

        String key = "traceId";
        MDC.put(key, UUID.randomUUID().toString());

        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Runnable task = () -> {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            contextMap.forEach((k, v) -> System.out.println("key=" + k + ", value=" + v));
        };

        // 提交任务到线程池
        executorService.submit(wrap(task));

        MDC.remove(key);
        // 关闭线程池
        executorService.shutdown();
    }

    public static <T> Callable<T> wrap(final Callable<T> callable) {
        return wrap(callable, MDC.getCopyOfContextMap());
    }

    public static Runnable wrap(final Runnable runnable) {
        return wrap(runnable, MDC.getCopyOfContextMap());
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            MDC.setContextMap(context);
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            MDC.setContextMap(context);
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
