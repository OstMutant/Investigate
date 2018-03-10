package org.ost.investigate.test.database.jdbc;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ost.investigate.test.database.Tools;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IsolationLevelsTest {


    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
        Tools.initJDBC(Tools.CONNECTION_BY_DEFAULT, Tools.PersonSQLContainer.createPersonTable(), Tools.PersonSQLContainer.insertNewPersonByDefault());
    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        Tools.destroyJDBC(Tools.CONNECTION_BY_DEFAULT, Tools.PersonSQLContainer.deletePersonTable());
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test simple select from Derby")
    void testSimpleSelect() throws Exception {
        try {
            Connection conn = Tools.prepareJDBCConn(Tools.CONNECTION_BY_DEFAULT);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String idCode = rs.getString(4);

                Assert.assertEquals(1, id);
                Assert.assertEquals(Tools.PersonSQLContainer.CONST_NAME, identity);
                Assert.assertEquals(Tools.PersonSQLContainer.CONST_EMAIL, name);
                Assert.assertEquals(Tools.PersonSQLContainer.CONST_ID_CODE, idCode);
                System.out.printf("%d %s %s %s\n", id, identity, name, idCode);
            }

            conn.commit();

        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
    }

    @Test
    @DisplayName("Test TRANSACTION_READ_UNCOMMITTED Isolation Level")
    void testTransactionReadUncommitted() throws Exception {
        try {
            Connection conn = Tools.prepareJDBCConn(Tools.CONNECTION_BY_DEFAULT);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String idCode = rs.getString(4);

                System.out.printf("before - %d %s %s %s\n", id, identity, name, idCode);
            }

            Connection connUpdate = Tools.prepareJDBCConn(Tools.CONNECTION_BY_DEFAULT);
            connUpdate.setAutoCommit(false);
            connUpdate.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            PreparedStatement pstmt = connUpdate.prepareStatement("update person set name=? where ?=1");
            pstmt.setString(1, "New value");
            pstmt.setInt(2, 1);
            pstmt.executeUpdate();
            System.out.println("Value is updated");

            Thread.sleep(1000);

            rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String idCode = rs.getString(4);

                System.out.printf("after - %d %s %s %s\n", id, identity, name, idCode);
            }

            connUpdate.commit();
            conn.commit();
            System.out.println("Transactions are commited");

        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
    }

    @Test
    @DisplayName("Test TRANSACTION_READ_COMMITTED Isolation Level")
    void testTransactionReadCommitted() throws Exception {
        try {
            Connection conn = Tools.prepareJDBCConn(Tools.CONNECTION_BY_DEFAULT);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String idCode = rs.getString(4);

                System.out.printf("before - %d %s %s %s\n", id, identity, name, idCode);
            }

            Connection connUpdate = Tools.prepareJDBCConn(Tools.CONNECTION_BY_DEFAULT);
            connUpdate.setAutoCommit(false);
            connUpdate.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            PreparedStatement pstmt = connUpdate.prepareStatement("update person set name=? where ?=1");
            pstmt.setString(1, "New value");
            pstmt.setInt(2, 1);
            pstmt.executeUpdate();
            connUpdate.commit();
            System.out.println("Value is updated");

            Thread.sleep(1000);

            rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String idCode = rs.getString(4);

                System.out.printf("after - %d %s %s %s\n", id, identity, name, idCode);
            }

            conn.commit();
            System.out.println("Main transaction are commited");

        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
    }

    @Test
    @DisplayName("Test TRANSACTION_REPEATABLE_READ Isolation Level")
    void testTransactionRepeatableRead() throws Exception {
        try {
            Connection conn = Tools.prepareJDBCConn(Tools.CONNECTION_BY_DEFAULT);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String idCode = rs.getString(4);

                System.out.printf("before - %d %s %s %s\n", id, identity, name, idCode);
            }

            Connection connInsert = Tools.prepareJDBCConn(Tools.CONNECTION_BY_DEFAULT);
            PreparedStatement pstmt = connInsert.prepareStatement("insert into person (name,email,idCode) values(?,?,?)");
            pstmt.setString(1, Tools.PersonSQLContainer.CONST_NAME + "1");
            pstmt.setString(2, Tools.PersonSQLContainer.CONST_EMAIL);
            pstmt.setString(3, Tools.PersonSQLContainer.CONST_ID_CODE);
            pstmt.executeUpdate();
            connInsert.commit();
            System.out.println("Value is updated");

            Thread.sleep(1000);

            rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String idCode = rs.getString(4);

                System.out.printf("after - %d %s %s %s\n", id, identity, name, idCode);
            }

            conn.commit();
            System.out.println("Main transaction are commited");

        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
    }
}
