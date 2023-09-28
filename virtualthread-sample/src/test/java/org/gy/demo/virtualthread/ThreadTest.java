package org.gy.demo.virtualthread;

import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Threads(4)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class ThreadTest {

    @Param({"500", "1000", "2000"})
    private int loop;

    @Param({"50", "100", "200"})
    private int nThreads;

    private static final int sleepTimeMillis = 30;

    private static final String PLATFORM_PREFIX = "PlatformThreadTest";
    private static final String VIRTUAL_PREFIX = "VirtualThreadTest";

    private ExecutorService executor;
    private ExecutorService virtualExecutor;

    @Setup
    public void setup() {
        //平台线程方式
        executor = Executors.newFixedThreadPool(nThreads);

        //定义虚拟线程调度器大小，保持跟平台线程池大小一样
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", String.valueOf(nThreads));
        virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @TearDown
    public void tearDown() {
        executor.close();
        virtualExecutor.close();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(ThreadTest.class.getSimpleName()).build();
        new Runner(opt).run();

//        threadExecute(200, 100, VIRTUAL_PREFIX);
//        threadExecute(200, 100, PLATFORM_PREFIX);
    }

    @Benchmark
    public void platformThreadTest(Blackhole bh) {
        List<Integer> result = execute(loop, executor, ThreadTest::sleepTime);
        bh.consume(result);
    }

    @Benchmark
    public void virtualThreadTest(Blackhole bh) {
        List<Integer> result = execute(loop, virtualExecutor, ThreadTest::sleepTime);
        bh.consume(result);
    }

    private static <T> List<T> execute(int loop, ExecutorService executor, Supplier<T> supplier) {
        CompletableFuture<T>[] futures = new CompletableFuture[loop];
        for (int i = 0; i < loop; i++) {
            //模拟执行耗时任务
            futures[i] = CompletableFuture.supplyAsync(supplier, executor);
        }
        CompletableFuture<Void> result = CompletableFuture.allOf(futures);
        result.join();
        return Stream.of(futures).map(f -> f.getNow(null)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SneakyThrows
    private static int sleepTime() {
        Thread.sleep(Duration.ofMillis(sleepTimeMillis));
        return sleepTimeMillis;
    }

    private static void threadExecute(int loop, int nThreads, String type) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> ThreadTest.printThreadInfo(type), 1, 1, TimeUnit.SECONDS);

        ExecutorService executorService;
        if (VIRTUAL_PREFIX.equals(type)) {
            ThreadFactory virtualFactory = Thread.ofVirtual().name(type, 0).factory();
            executorService = Executors.newThreadPerTaskExecutor(virtualFactory);
        } else {
            ThreadFactory platformFactory = Thread.ofPlatform().name(type, 0).factory();
            executorService = Executors.newFixedThreadPool(100, platformFactory);
        }

        long start = System.currentTimeMillis();
        List<Integer> result = execute(loop, executorService, ThreadTest::sleepTime);
        System.out.println("cost time: " + (System.currentTimeMillis() - start) + "ms\t result size: " + result.size());
        executorService.close();
    }

    private static void printThreadInfo(String type) {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfo = threadBean.dumpAllThreads(false, false);
        System.out.println("type: " + type + "\t thread count: " + threadInfo.length);
    }

}
