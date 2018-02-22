package org.ost.investigate.test;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Helper {
    public static class PrintBean {
        private String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        private Thread thread = Thread.currentThread();

        public void println(String s) {
            System.out.println(thread.toString() + " Object id - " + uuid + " --- " + s);
        }
    }

    public static void formatedPrint(String str) {
        Consumer<String> consumer = (s) -> ((Supplier<PrintBean>) Helper.PrintBean::new).get().println(s);
        consumer.accept(str);
    }
}
