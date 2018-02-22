package org.ost.investigate.test.threads;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ost.investigate.test.Helper;

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
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                int count = 0;
                while (count < 100) {
                    count = count + 1;
                    Helper.formatedPrint("count - " + count);
                    synchronized (lock) {
                        try {
                            lock.notifyAll();
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
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
            }
        });

        t1.start();
        t2.start();

        Thread.sleep(5000);
    }
}
