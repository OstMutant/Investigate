package org.ost.investigate.test.concurrent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ost.investigate.test.Helper;
import org.ost.investigate.test.Helper.PrintBean;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CompletableFutureTest {

    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Create object and use it for CompletableFuture")
    void testCompletableFuture() throws Exception {
        CompletableFuture.supplyAsync(() -> new PrintBean()).thenAccept((supplierLocal) -> supplierLocal.println("Hi! CompletableFuture"));
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Create object and use it for several CompletableFuture")
    void testCompletableFutures() throws Exception {
        Consumer<PrintBean> consumer = (prValue) -> prValue.println("Hi! CompletableFuture");
        PrintBean printBean = new PrintBean();
        Supplier<PrintBean> supplier = () -> printBean;
        CompletableFuture.supplyAsync(supplier).thenAccept(consumer);
        CompletableFuture.supplyAsync(supplier).thenAccept(consumer);
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Tests execute and submit SingleThreadExecutor with Runnable")
    void testSingleThreadExecutorsWithRunnable() throws Exception {
        Runnable run = () -> Helper.formatedPrint("Hi! Runnable");
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(run); // don't have any results
        Future<?> future = es.submit(run);
        future.get();
    }

}
