package org.ost.investigate.test.function.invokedynamic;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class InvokeDynamicTest {
    // https://findusages.com/search/java.lang.invoke.MethodHandles.Lookup/findSetter$3
    @BeforeEach
    void before() {
        System.out.println("-------------------------------------- Start");
    }

    @AfterEach
    void after() {
        System.out.println("-------------------------------------- Finish");
    }

    private class Person {
        public String name;

        public void print() {
            System.out.println("I am " + name);
        }
    }

    @Test
    @DisplayName("Invoke Dynamic Test")
    void testInvokeDynamic() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle setNameMethodHandle = lookup.findSetter(Person.class, "name", String.class);
        Person person = new Person();

        String name = "Charles";
        setNameMethodHandle.invoke(person, name);

        MethodHandle getNameMethodHandle = lookup.findGetter(Person.class, "name", String.class);
        String personName = (String) getNameMethodHandle.invoke(person);
        Assert.assertEquals(personName, name);
        MethodHandle printMethodHandle = lookup.findVirtual(Person.class, "print", MethodType.methodType(void.class));
        printMethodHandle.invoke(person);
    }
}
