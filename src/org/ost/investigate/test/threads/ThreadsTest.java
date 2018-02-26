package org.ost.investigate.test.threads;

import org.junit.jupiter.api.*;
import org.ost.investigate.test.Helper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadsTest {

    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Tests execute Threads with Wait and Notify")
    void testThreadWaitNotify() throws Exception {
        Object lock = new Object();

        class MyRunnable implements Runnable{
            public void run() {
                int count = 0;
                while (count < 100) {
                    count = count + 1;
                    Helper.formatedPrint("count - " + count);
                    synchronized (lock) {
                        try {
                            lock.notifyAll();
                            lock.wait();
                            Helper.formatedPrint("after wait, count - " + count);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }

        Thread t1 = new Thread(new MyRunnable());
        Thread t2 = new Thread(new MyRunnable());

        t1.start();
        t2.start();

        Thread.sleep(5000);
    }

    @Test
    @DisplayName("Tests SynchronousQueue")
    void testBlockingQueue() throws Exception {
        BlockingQueue blockingQueue = new SynchronousQueue();

        Thread t1 = new Thread(new Runnable(){
            public void run() {
                int count = 0;
                while (count < 100) {
                    count = count + 1;
                    try {
                        blockingQueue.put(true);
                        Helper.formatedPrint("write count - " + count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable(){
            public void run() {
                int count = 0;
                while (count < 100) {
                    count = count + 1;
                    try {
                        blockingQueue.take();
                        Helper.formatedPrint("read count - " + count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
        t2.start();

        Thread.sleep(5000);
    }

    @Test
    @DisplayName("Tests ReentrantLock")
    void testReentrantLock() throws Exception {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        class MyRunnable implements Runnable{
            public void run() {
                int count = 0;
                while (count < 100) {
                    count = count + 1;
                    reentrantLock.lock();
                    condition.signalAll();
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Helper.formatedPrint("count - " + count);
                    reentrantLock.unlock();
                }
                reentrantLock.lock();
                condition.signalAll();
                reentrantLock.unlock();
            }
        }

        Thread t1 = new Thread(new MyRunnable());
        Thread t2 = new Thread(new MyRunnable());

        t1.start();
        t2.start();

        Thread.sleep(5000);
    }
}
