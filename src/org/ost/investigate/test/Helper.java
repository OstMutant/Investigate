package org.ost.investigate.test;

import java.util.UUID;

public class Helper {
    public static class PrintBean {
        private String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        private Thread thread = Thread.currentThread();

        public void println(String s) {
            System.out.println(thread.toString() + " Object id - " + uuid + " --- " + s);
        }
    }
}
