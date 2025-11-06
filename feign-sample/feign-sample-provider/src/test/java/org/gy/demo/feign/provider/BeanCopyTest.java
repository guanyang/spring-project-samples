package org.gy.demo.feign.provider;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/5/12 10:53
 */
@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Threads(1)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class BeanCopyTest {

    @Param({"100", "1000"})
    private int loop;

    private List<SourceBean> sources;

    private static final BeanCopier beanCopier = BeanCopier.create(SourceBean.class, TargetBean.class, false);

    @Setup
    public void setup() {
        sources = buildSources(loop);
    }

    @TearDown
    public void tearDown() {
        sources = null;
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public void apacheBeanCopy(Blackhole bh) {
        List<TargetBean> targetBeans = doCopy(sources, s -> {
            TargetBean targetBean = new TargetBean();
            try {
                org.apache.commons.beanutils.BeanUtils.copyProperties(targetBean, s);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return targetBean;
        });
        bh.consume(targetBeans);
    }

    @Benchmark
    public void beanCopierBeanCopy(Blackhole bh) {
        List<TargetBean> targetBeans = doCopy(sources, (s, t) -> beanCopier.copy(s, t, null));
        bh.consume(targetBeans);
    }

    @Benchmark
    public void fastJsonBeanCopy(Blackhole bh) {
        List<TargetBean> targetBeans = doCopy(sources, s -> JSON.parseObject(JSON.toJSONString(s), TargetBean.class));
        bh.consume(targetBeans);
    }

    @Benchmark
    public void mapStructBeanCopy(Blackhole bh) {
        List<TargetBean> targetBeans = doCopy(sources, BeanMapping.INSTANCE::map);
        bh.consume(targetBeans);
    }

    @Benchmark
    public void springBeanCopy(Blackhole bh) {
        List<TargetBean> targetBeans = doCopy(sources, BeanUtils::copyProperties);
        bh.consume(targetBeans);
    }

    @Benchmark
    public void hutoolBeanCopy(Blackhole bh) {
        List<TargetBean> targetBeans = doCopy(sources, BeanUtil::copyProperties);
        bh.consume(targetBeans);
    }

    @Benchmark
    public void getSetBeanCopy(Blackhole bh) {
        List<TargetBean> targetBeans = doCopy(sources, BeanCopyTest::getSetMapping);
        bh.consume(targetBeans);
    }


    private static List<TargetBean> doCopy(List<SourceBean> sources, Function<SourceBean, TargetBean> beanCopy) {
        return sources.stream().map(beanCopy).collect(Collectors.toList());
    }

    private static List<TargetBean> doCopy(List<SourceBean> sources, BiConsumer<SourceBean, TargetBean> beanCopy) {
        return sources.stream().map(s -> {
            TargetBean targetBean = new TargetBean();
            beanCopy.accept(s, targetBean);
            return targetBean;
        }).collect(Collectors.toList());
    }

    private static List<SourceBean> buildSources(Integer num) {
        List<SourceBean> sources = Lists.newArrayList();
        for (int j = 0; j < num; j++) {
            int index = ThreadLocalRandom.current().nextInt();
            boolean flag = ThreadLocalRandom.current().nextBoolean();
            SourceBean sourceBean = new SourceBean();
            sourceBean.setAge(index);
            sourceBean.setTitle("title" + index);
            sourceBean.setSource("source" + index);
            sourceBean.setEat(flag);
            sourceBean.setStart(new Date());
            sourceBean.setItem(new Item(System.currentTimeMillis(), System.currentTimeMillis()));
            sources.add(sourceBean);
        }
        return sources;
    }

    public static TargetBean getSetMapping(SourceBean source) {
        TargetBean targetBean = new TargetBean();

        targetBean.setAge(source.getAge());
        targetBean.setTitle(source.getTitle());
        targetBean.setEat(source.getEat());
        targetBean.setStart(source.getStart());
        targetBean.setItem(source.getItem());

        return targetBean;
    }

    @Mapper
    interface BeanMapping {

        BeanMapping INSTANCE = Mappers.getMapper(BeanMapping.class);

        TargetBean map(SourceBean source);

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SourceBean {

        private Integer age;

        private String title;

        private String source;

        private Boolean eat;

        private Date start;

        private Item item;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TargetBean {

        private Integer age;

        private String title;

        private String target;

        private Boolean eat;

        private Date start;

        private Item item;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        private Long startTime;

        private Long endTime;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

}
