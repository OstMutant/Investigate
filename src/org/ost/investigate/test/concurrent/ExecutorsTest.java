package org.ost.investigate.test.concurrent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ost.investigate.test.Helper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorsTest {
    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Tests execute and submit SingleThreadExecutor with Callable")
    void testSingleThreadExecutorsWithCallable() throws Exception {
        Callable<String> callable = () -> {
            return "Return the result of Callable";
        };
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(callable);
        Helper.formatedPrint(future.get());
    }

    @Test
    @DisplayName("Tests execute and submit SingleThreadExecutor with Callable and Exception")
    void testSingleThreadExecutorsWithCallableAndException() throws Exception {
        Callable<String> callable = () -> {
            throw new Exception("It is exception :)");
        };
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(callable);
        try {
            Helper.formatedPrint(future.get());
        } catch (Exception ex) {
            Helper.formatedPrint(ex.getMessage());
        }
    }
}
