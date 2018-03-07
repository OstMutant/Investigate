package org.ost.investigate.test.database.JDBC;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IsolationLevelsTest {

    private static final String CONST_IDENTITY = "Hagar the Horrible";
    private static final String CONST_EMAIL = "hagar@somewhere.com";
    private static final String CONST_PHONE = "1234567890";

    private Connection prepareConn() throws SQLException {
        Driver derbyEmbeddedDriver = new EmbeddedDriver();
        DriverManager.registerDriver(derbyEmbeddedDriver);
        return DriverManager.getConnection("jdbc:derby:testdb1;create=true", "test", "pass123");
    }

    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
        try {
            Connection conn = prepareConn();
            conn.setAutoCommit(false);

            Statement stmt;
//            stmt = conn.createStatement();
//            stmt.execute("drop table person");

            stmt = conn.createStatement();
            String createSQL = "create table person ("
                    + "id integer not null generated always as"
                    + " identity (start with 1, increment by 1),   "
                    + "name varchar(30) not null, email varchar(30), phone varchar(10),"
                    + "constraint primary_key primary key (id))";
            stmt.execute(createSQL);

            PreparedStatement pstmt = conn.prepareStatement("insert into person (name,email,phone) values(?,?,?)");
            pstmt.setString(1, CONST_IDENTITY);
            pstmt.setString(2, CONST_EMAIL);
            pstmt.setString(3, CONST_PHONE);
            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        try {
            Connection conn = prepareConn();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.execute("drop table person");
            conn.commit();
        } catch (SQLException ex) {
            System.out.println("Can't drop table person. In connection" + ex);
        }
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            if (((ex.getErrorCode() == 50000) && ("XJ015".equals(ex.getSQLState())))) {
                System.out.println("Derby shut down normally");
            } else {
                System.err.println("Derby did not shut down normally");
                System.err.println(ex.getMessage());
            }
        }
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test simple select from Derby")
    void testSimpleSelect() throws Exception {
        try {
            Connection conn = prepareConn();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String phone = rs.getString(4);

                Assert.assertEquals(1, id);
                Assert.assertEquals(CONST_IDENTITY, identity);
                Assert.assertEquals(CONST_EMAIL, name);
                Assert.assertEquals(CONST_PHONE, phone);
                System.out.printf("%d %s %s %s\n", id, identity, name, phone);
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
            Connection conn = prepareConn();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String phone = rs.getString(4);

                System.out.printf("before - %d %s %s %s\n", id, identity, name, phone);
            }

            Connection connUpdate = prepareConn();
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
                String phone = rs.getString(4);

                System.out.printf("after - %d %s %s %s\n", id, identity, name, phone);
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
            Connection conn = prepareConn();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String phone = rs.getString(4);

                System.out.printf("before - %d %s %s %s\n", id, identity, name, phone);
            }

            Connection connUpdate = prepareConn();
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
                String phone = rs.getString(4);

                System.out.printf("after - %d %s %s %s\n", id, identity, name, phone);
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
            Connection conn = prepareConn();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String phone = rs.getString(4);

                System.out.printf("before - %d %s %s %s\n", id, identity, name, phone);
            }

            Connection connInsert = prepareConn();
            PreparedStatement pstmt = connInsert.prepareStatement("insert into person (name,email,phone) values(?,?,?)");
            pstmt.setString(1, CONST_IDENTITY + "1");
            pstmt.setString(2, CONST_EMAIL);
            pstmt.setString(3, CONST_PHONE);
            pstmt.executeUpdate();
            connInsert.commit();
            System.out.println("Value is updated");

            Thread.sleep(1000);

            rs = stmt.executeQuery("select * from person");
            while (rs.next()) {
                int id = rs.getInt(1);
                String identity = rs.getString(2);
                String name = rs.getString(3);
                String phone = rs.getString(4);

                System.out.printf("after - %d %s %s %s\n", id, identity, name, phone);
            }

            conn.commit();
            System.out.println("Main transaction are commited");

        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
    }
}
