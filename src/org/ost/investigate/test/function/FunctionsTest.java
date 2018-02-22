package org.ost.investigate.test.function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ost.investigate.test.Helper.PrintBean;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FunctionsTest {
    private String CONGRATULATION_SUPPLIER = "Hi! Supplier";
    private String CONGRATULATION_CONSUMER = "Hi! Consumer";

    @BeforeEach
    void before() {
        System.out.println("-------------------------------------- Start");
    }

    @AfterEach
    void after() {
        System.out.println("-------------------------------------- Finish");
    }

    @Test
    void testNewSupplier() {
        Supplier<PrintBean> supplier = () -> new PrintBean();
        supplier.get().println(CONGRATULATION_SUPPLIER);
    }

    @Test
    void testSuppliers() {
        PrintBean printBean = new PrintBean();

        Supplier<PrintBean> supplier = () -> printBean;
        supplier.get().println(CONGRATULATION_SUPPLIER);

        supplier = () -> printBean;
        supplier.get().println(CONGRATULATION_SUPPLIER);
    }

    @Test
    void testConsumer() {
        Consumer<PrintBean> consumer = (supplierLocal) -> supplierLocal.println(CONGRATULATION_CONSUMER);
        consumer.accept(new PrintBean());
    }

    @Test
    void testConsumers() {
        Consumer<PrintBean> consumer = (supplierLocal) -> supplierLocal.println(CONGRATULATION_CONSUMER);
        consumer.accept(new PrintBean());
        consumer.accept(new PrintBean());
    }
}
