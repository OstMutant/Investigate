package org.ost.investigate.test.collections;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.util.IdentityHashMap;

public class IdentityHashMapTest {

    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("IdentityHashMap")
    void testSizeIdentityHashMap() throws Exception {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        identityHashMap.put("test1","testValue1");
        identityHashMap.put("test1","testValue1");
        identityHashMap.put("test2","testValue2");
        Assert.assertEquals(identityHashMap.size(), 2);
    }

    @Test
    @DisplayName("IdentityHashMap")
    void testSizeIdentityHashMapIfNewString() throws Exception {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        identityHashMap.put(new String("test1"),"testValue1");
        identityHashMap.put(new String("test1"),"testValue1");
        identityHashMap.put(new String("test2"),"testValue2");
        Assert.assertEquals(identityHashMap.size(), 3);
    }

    @Test
    @DisplayName("IdentityHashMap")
    void testSizeIdentityHashMapIfNewStringAndIntern() throws Exception {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        identityHashMap.put(new String("test1").intern(),"testValue1");
        identityHashMap.put(new String("test1").intern(),"testValue1");
        identityHashMap.put(new String("test2"),"testValue2");
        Assert.assertEquals(identityHashMap.size(), 2);
    }
}
