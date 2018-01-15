package org.ost.investigate.concurrent;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Supplier;

public class CompletableFutureTest {

    private static class PrintBean {
        private String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        private Thread thread = Thread.currentThread();

        public Thread getThread(){
            return thread;
        }

        public void println(String s){
            System.out.println(thread.toString() + " Object id - " + uuid + " --- " + s);
        }
    }

    @Test
    void testSupplier() {
        Supplier<PrintBean> supplier = () -> new PrintBean();
        supplier.get().println("Hi! Supplier");
    }

}
