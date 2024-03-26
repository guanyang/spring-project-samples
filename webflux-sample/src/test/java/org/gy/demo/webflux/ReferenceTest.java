package org.gy.demo.webflux;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceTest {

    public static void main(String[] args) throws InterruptedException {
        //参考文档：https://zhuanlan.zhihu.com/p/85576999

        //软引用在堆内存不足时，会被回收
//        testSoftReference();

        //弱引用在每次gc之后都会被回收
//        testWeakReference();

        //引用队列（ReferenceQueue）作用：在于我们在外部可以对queue中的引用进行监控，当引用中的对象被回收后，我们可以对引用对象本身继续做一些清理操作，因为我们引用对象也占有一定的资源。
//        testWeakReferenceAndQueue();

        //虚引用在任何时候都可能被回收，主要用来跟踪对象的回收，清理被销毁对象的相关资源
        testPhantomReference();
    }

    public static void testSoftReference() {
        Object obj = new Object();
        SoftReference<Object> softRef = new SoftReference<>(obj);

        //删除强引用
        obj = null;
        System.out.println("gc之前的值：" + softRef.get()); // 对象依然存在

        //调用gc
        System.gc();
        System.out.println("gc之后的值：" + softRef.get()); // 对象依然存在
    }


    public static void testWeakReference() {
        Object obj = new Object();
        WeakReference<Object> weakRef = new WeakReference<>(obj);

        //删除强引用
        obj = null;
        System.out.println("gc之前的值：" + weakRef.get()); // 对象依然存在

        //调用gc
        System.gc();
        System.out.println("gc之后的值：" + weakRef.get()); // 对象为null
    }

    public static void testWeakReferenceAndQueue() throws InterruptedException {
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        Object obj = new Object();
        WeakReference<Object> weakRef = new WeakReference<>(obj, queue);
        //删除强引用
        obj = null;
        System.out.println("gc之前的值: " + weakRef.get()); // 对象依然存在

        //调用gc
        System.gc();
        //如果obj被回收，则软引用会进入引用队列
        Reference<?> reference = queue.remove();
        if (reference != null) {
            System.out.println("对象已被回收: " + reference.get());  // 对象为null
        }
    }

    public static void testPhantomReference() throws InterruptedException {
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        Object obj = new Object();
        PhantomReference<Object> phantomRef = new PhantomReference<>(obj, queue);

        //删除强引用
        obj = null;
        System.out.println("gc之前的值：" + phantomRef.get()); // 方法永远返回null

        //调用gc
        System.gc();
        //如果obj被回收，则软引用会进入引用队列
        Reference<?> reference = queue.remove();
        if (reference != null) {
            System.out.println("对象已被回收: " + reference.get());  // 对象为null
        }
    }

}
