package org.ost.investigate.test.concurrent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ost.investigate.test.Helper.PrintBean;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CompletableFutureTest {

    private String CONGRATULATION_COMPLETABLE_FUTURE = "Hi! CompletableFuture";

    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
    }

    @AfterEach
    void after(TestInfo testInfo) {
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Create object and use it for CompletableFuture")
    void testCompletableFuture() throws Exception {
        CompletableFuture.supplyAsync(() -> new PrintBean()).thenAccept((supplierLocal) -> supplierLocal.println(CONGRATULATION_COMPLETABLE_FUTURE));
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Create object and use it for several CompletableFuture")
    void testCompletableFutures() throws Exception {
        Consumer<PrintBean> consumer = (supplier) -> supplier.println(CONGRATULATION_COMPLETABLE_FUTURE);
        PrintBean printBean = new PrintBean();
        Supplier<PrintBean> supplier = () -> printBean;
        CompletableFuture.supplyAsync(supplier).thenAccept(consumer);
        CompletableFuture.supplyAsync(supplier).thenAccept(consumer);
        Thread.sleep(1000);
    }

}
