package org.gy.demo.webflux;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintTest {

    public static class PrintABCUsingLockCondition {

        private final int times;

        private int state;


        public PrintABCUsingLockCondition(int times) {
            this.times = times;
        }

        public void print(String name, int targetState, Lock lock, Condition cur, Condition next) {
            for (int i = 0; i < times; i++) {
                lock.lock();
                try {
                    while (state % 3 != targetState) {
                        try {
                            cur.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(name);
                    state++;
                    next.signal();
                } finally {
                    lock.unlock();
                }
            }
        }

        public static void main(String[] args) {
            PrintABCUsingLockCondition printABCUsingLockCondition = new PrintABCUsingLockCondition(2);
            Lock lock = new ReentrantLock();
            Condition conditionA = lock.newCondition();
            Condition conditionB = lock.newCondition();
            Condition conditionC = lock.newCondition();
            new Thread(() -> printABCUsingLockCondition.print("A", 0, lock, conditionA, conditionB)).start();
            new Thread(() -> printABCUsingLockCondition.print("B", 1, lock, conditionB, conditionC)).start();
            new Thread(() -> printABCUsingLockCondition.print("C", 2, lock, conditionC, conditionA)).start();
        }

    }


    public static class NumAndLetterPrinter {

        private final int times;

        private int state;

        private final Object lock = new Object();

        private static final char c = 'A';

        private static final String NUM_KEY = "NUM";
        private static final String LETTER_KEY = "LETTER";

        public NumAndLetterPrinter(int times) {
            this.times = times;
        }

        public void print(String name, int targetState) {
            for (int i = 0; i < times; i++) {
                synchronized (lock) {
                    while (state % 2 != targetState) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (NUM_KEY.equals(name)) {
                        System.out.print(i + 1);
                    } else {
                        System.out.print((char) (c + i));
                    }
                    state++;
                    lock.notifyAll();
                }
            }
        }

        public static void main(String[] args) {
            NumAndLetterPrinter numAndLetterPrinter = new NumAndLetterPrinter(26);
            new Thread(() -> numAndLetterPrinter.print(NUM_KEY, 0)).start();
            new Thread(() -> numAndLetterPrinter.print(LETTER_KEY, 1)).start();
        }
    }

    public static class OddEvenPrinter {

        private final int times;
        private volatile int state;
        private final Object lock = new Object();

        public OddEvenPrinter(int times, int state) {
            this.times = times;
            this.state = state;
        }

        public void print(String name, int targetState) {
            for (int i = 0; i < times; i++) {
                synchronized (lock) {
                    while (state % 2 != targetState) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(String.format("线程[%s]打印数字:%d", name, state++));
                    lock.notifyAll();
                }
            }
        }

        public static void main(String[] args) {
            OddEvenPrinter oddEvenPrinter = new OddEvenPrinter(5, 0);
            new Thread(() -> oddEvenPrinter.print("奇数", 1)).start();
            new Thread(() -> oddEvenPrinter.print("偶数", 0)).start();
        }

    }


    public static class PrintABCUsingWaitNotify {

        private final int times;

        private int state;

        private final Object lock = new Object();

        public PrintABCUsingWaitNotify(int times) {
            this.times = times;
        }

        public void print(String name, int targetState) {
            for (int i = 0; i < times; i++) {
                synchronized (lock) {
                    while (state % 3 != targetState) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(name);
                    state++;
                    lock.notifyAll();
                }
            }
        }

        public static void main(String[] args) {
            PrintABCUsingWaitNotify printABCUsingWaitNotify = new PrintABCUsingWaitNotify(5);
            new Thread(() -> printABCUsingWaitNotify.print("A", 0)).start();
            new Thread(() -> printABCUsingWaitNotify.print("B", 1)).start();
            new Thread(() -> printABCUsingWaitNotify.print("C", 2)).start();
        }
    }

    public static class PrintABCUsingLock {

        private final int times;

        private int state;

        private final Lock lock = new ReentrantLock();

        public PrintABCUsingLock(int times) {
            this.times = times;
        }

        public void print(String name, int targetState) {
            int loop = 0;
            while (loop < times) {
                lock.lock();
                try {
                    if (state % 3 == targetState) {
                        System.out.println(name);
                        state++;
                        loop++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        public static void main(String[] args) {
            PrintABCUsingLock printABCUsingLock = new PrintABCUsingLock(5);
            new Thread(() -> printABCUsingLock.print("A", 0)).start();
            new Thread(() -> printABCUsingLock.print("B", 1)).start();
            new Thread(() -> printABCUsingLock.print("C", 2)).start();
        }
    }

}
