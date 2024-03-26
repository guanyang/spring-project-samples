package org.gy.demo.webflux;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadLocalDemo {

    public static void main(String[] args) {

//        threadLocalTest();
//        inheritableThreadLocalTest();

//        transmittableThreadLocal();

        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 3; i++) {
            customThreadLocal(String.valueOf(i), executorService);
        }
        executorService.shutdown();
    }

    public static class ThreadLocalUtil {

        public static <T> Callable<T> wrap(final Callable<T> callable) {
            // 获取父线程的局部变量
            Object parentValue = ContextHolder.get();
            return () -> {
                try {
                    // 设置子线程的局部变量
                    ContextHolder.set(parentValue);
                    // 执行任务
                    return callable.call();
                } finally {
                    // 清理子线程的局部变量
                    ContextHolder.remove();
                }
            };
        }

        public static Runnable wrap(final Runnable runnable) {
            // 获取父线程的局部变量
            Object parentValue = ContextHolder.get();
            return () -> {
                try {
                    // 设置子线程的局部变量
                    ContextHolder.set(parentValue);
                    // 执行任务
                    runnable.run();
                } finally {
                    // 清理子线程的局部变量
                    ContextHolder.remove();
                }
            };
        }
    }

    public static class ContextHolder {

        public static ThreadLocal<Object> context = new InheritableThreadLocal<>();

        public static void set(Object obj) {
            context.set(obj);
        }

        public static Object get() {
            return context.get();
        }

        public static void remove() {
            context.remove();
        }
    }

    public static void customThreadLocal(String key, ExecutorService executorService) {
        // 设置线程局部变量
        ContextHolder.set(key + "-父线程的数据：" + UUID.randomUUID());
        try {
            Runnable task = () -> {
                // 在子线程中访问之前在父线程中设置的线程局部变量
                System.out.println(key + "-子线程中的数据: " + ContextHolder.get());
            };

            // 使用 TtlRunnable 包装原始任务，以支持 TransmittableThreadLocal
            Runnable ttlTask = ThreadLocalUtil.wrap(task);

            // 提交任务到线程池
            executorService.submit(ttlTask);

            System.out.println(key + "-父线程中的数据: " + ContextHolder.get());
        } finally {
            ContextHolder.remove();
        }
    }

    public static void transmittableThreadLocal() {
        //会有线程安全问题，因为默认都是引用类型拷贝，如果子线程修改了数据，主线程是可以感知到的
        TransmittableThreadLocal<AtomicReference<String>> context = new TransmittableThreadLocal<>();
        // 设置线程局部变量
        context.set(new AtomicReference<>("这是父线程的数据"));

        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Runnable task = () -> {
            // 在子线程中访问之前在父线程中设置的线程局部变量
            AtomicReference<String> reference = context.get();
            System.out.println("[transmittableThreadLocal]子线程中的数据1: " + reference);

            reference.set("这是子线程的数据");
            System.out.println("[transmittableThreadLocal]子线程中的数据2: " + context.get());
        };

        // 使用 TtlRunnable 包装原始任务，以支持 TransmittableThreadLocal
        Runnable ttlTask = TtlRunnable.get(task);

        // 提交任务到线程池
        executorService.submit(ttlTask);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("[transmittableThreadLocal]父线程中的数据: " + context.get());
        context.remove();

        // 关闭线程池
        executorService.shutdown();
    }

    public static void inheritableThreadLocalTest() {
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        // 在父线程中设置 InheritableThreadLocal 变量的值
        inheritableThreadLocal.set("这是来自父线程的数据");

        // 创建并启动一个子线程
        Thread childThread = new Thread(() -> {
            // 在子线程中获取并打印 InheritableThreadLocal 变量的值
            System.out.println("[inheritableThreadLocalTest]子线程中的数据: " + inheritableThreadLocal.get());
        });

        childThread.start();

        // 等待子线程执行完成
        try {
            childThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("主线程被中断");
        }

        // 清理资源
        inheritableThreadLocal.remove();

    }

    public static void threadLocalTest() {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        // 在父线程中设置 ThreadLocal 变量的值
        threadLocal.set("这是来自父线程的数据");

        // 创建并启动一个子线程
        Thread childThread = new Thread(() -> {
            // 在子线程中获取并打印 ThreadLocal 变量的值
            System.out.println("[threadLocalTest]子线程中的数据: " + threadLocal.get());
        });

        childThread.start();

        // 等待子线程执行完成
        try {
            childThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 清理资源
        threadLocal.remove();
    }

}
