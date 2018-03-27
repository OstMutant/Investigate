package org.ost.investigate.test.rmi;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RMITest {

    @BeforeAll
    void beforeAll() {
        ServerOperation.apply();
        System.out.println("-------------------------------------- Before All");
    }

    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start " + testInfo.getDisplayName());
    }

    @AfterEach
    void after(TestInfo testInfo) {
        System.out.println("-------------------------------------- Finish " + testInfo.getDisplayName());
    }

    @AfterAll
    void afterAll() {
        System.out.println("-------------------------------------- After All");
    }

    @Test
    @DisplayName("Test RMI")
    void testRMIClient() throws RemoteException, NotBoundException{
        Registry rmiRegistry = LocateRegistry.getRegistry(ServerOperation.PORT);
        RMIInterface look_up = (RMIInterface) rmiRegistry.lookup(ServerOperation.NAME);

        String testName = "TestName";
        String response = look_up.helloTo(testName);
        Assert.assertEquals(String.format(ServerOperation.OUTCOME, testName), response);
        System.out.println("Response - " + response);
    }
}
