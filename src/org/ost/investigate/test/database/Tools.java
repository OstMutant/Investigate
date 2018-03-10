package org.ost.investigate.test.database;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Tools {

    public interface ConnectionDataSeter {
        void setUrl(String url);

        void setUser(String user);

        void setPassword(String password);
    }

    public interface ConnectionDataGetter {

        String getUrl();

        String getUser();

        String getPassword();
    }

    public static class ConnectionBean implements ConnectionDataGetter {
        public final String CONST_URL = "jdbc:derby:testdb1;create=true";
        public final String CONST_USER = "test";
        public final String CONST_PASSWORD = "pass123";

        private String url = CONST_URL;
        private String user = CONST_USER;
        private String password = CONST_PASSWORD;

        public String getUrl() {
            return url;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }
    }


    public static class PhoneSQLContainer {

//        public static final String CONST_CODE = "0123456789";

//        public static List<String> createIdCodeTable() {
//            List result = new ArrayList();
//            String sql = "create table phone (id integer not null generated always as identity (start with 1, increment by 1), number varchar(10), constraint phone_pk primary key (id), constraint person_fk foreign key (id) references person(id))";
//            result.add(sql);
//            return result;
//        }

        public static List<String> deleteIdCodeTable() {
            List result = new ArrayList();
            String sql = "drop table phone";
            result.add(sql);
            return result;
        }
    }

    public static class PersonSQLContainer {

        public static final String CONST_NAME = "Hagar the Horrible";
        public static final String CONST_EMAIL = "hagar@somewhere.com";
        public static final String CONST_ID_CODE = "1234567890";

        public static List<String> createPersonTable() {
            List result = new ArrayList();
            String sql = "create table person (id integer not null generated always as identity (start with 1, increment by 1), name varchar(30) not null, email varchar(30), idCode varchar(10), constraint person_pk primary key (id))";
            result.add(sql);
            return result;
        }

        public static List<String> deletePersonTable() {
            List result = new ArrayList();
            String sql = "drop table person";
            result.add(sql);
            return result;
        }

        public static FunctionWithException<Connection, List<PreparedStatement>> insertNewPersonByDefault() {
            return (conn) -> {
                PreparedStatement pstmt = conn.prepareStatement("insert into person (name,email,idcode) values(?,?,?)");
                pstmt.setString(1, CONST_NAME);
                pstmt.setString(2, CONST_EMAIL);
                pstmt.setString(3, CONST_ID_CODE);
                return Arrays.asList(pstmt);
            };
        }
    }

    public static final ConnectionBean CONNECTION_BY_DEFAULT = new ConnectionBean();

    public static Connection prepareJDBCConn(ConnectionDataGetter connectionData) throws SQLException {
        Driver derbyEmbeddedDriver = new EmbeddedDriver();
        DriverManager.registerDriver(derbyEmbeddedDriver);
        return DriverManager.getConnection(connectionData.getUrl(), connectionData.getUser(), connectionData.getPassword());
    }

    public interface FunctionWithException<T, R> {
        R apply(T t) throws SQLException;
    }

    public interface ConsumerWithException<T> {
        void accept(T t) throws SQLException;
    }

    public static void initJDBC(ConnectionDataGetter connectionData, List<String> sqlStatements, FunctionWithException<Connection, List<PreparedStatement>> preparedStatementFunction) {
        System.out.println("Derby go up");
        try {
            Connection conn = prepareJDBCConn(connectionData);
            conn.setAutoCommit(false);

            if (sqlStatements != null) {
                ConsumerWithException<String> statementExecutor = (sql) -> {
                    Statement stmt = conn.createStatement();
                    stmt.execute(sql);
                };

                for (String sql : sqlStatements) {
                    statementExecutor.accept(sql);
                }
            }

            if (preparedStatementFunction != null) {
                ConsumerWithException<PreparedStatement> preparedStatementExecutor = (pstmt) -> {
                    pstmt.executeUpdate();
                };

                for (PreparedStatement pstmt : preparedStatementFunction.apply(conn)) {
                    preparedStatementExecutor.accept(pstmt);
                }
            }

            conn.commit();

        } catch (SQLException ex) {
            System.out.println("Error in connection" + ex);
        }
        System.out.println("Derby went up normally");
    }

    public static void destroyJDBC(ConnectionDataGetter connectionData, List<String> sqlStatements) throws InterruptedException {
        System.out.println("Derby shutting down");
        try {
            Connection conn = prepareJDBCConn(connectionData);
            conn.setAutoCommit(false);

            ConsumerWithException<String> statementExecutor = (sql) -> {
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            };

            for (String sql : sqlStatements) {
                try {
                    statementExecutor.accept(sql);
                } catch (SQLException ex) {
                    System.out.println(sql + " can't be performed " + ex);
                }
            }

            conn.commit();
        } catch (SQLException ex) {
            System.out.println("Error in connection" + ex);
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
    }

    public static EntityManager createEntityManager(ConnectionDataGetter connectionData, List<String> managedClassNames) {

        Properties props = new Properties();
        props.put("hibernate.connection.url", connectionData.getUrl());
        props.put("hibernate.connection.username", connectionData.getUser());
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", true);

        if (!connectionData.getPassword().isEmpty()) {
            props.put("hibernate.connection.password", connectionData.getPassword());
        }

        PersistenceUnitInfo persistenceUnitInfo = new PersistenceUnitInfo() {

            @Override
            public Properties getProperties() {
                return props;
            }

            @Override
            public List<String> getManagedClassNames() {
                return managedClassNames;
            }

            @Override
            public String getPersistenceUnitName() {
                return "TestUnit";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return HibernatePersistenceProvider.class.getName();
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return null;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }

            @Override
            public List<String> getMappingFileNames() {
                return null;
            }

            @Override
            public List<URL> getJarFileUrls() {
                return null;
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {
            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };

        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();

        EntityManagerFactory entityManagerFactory = hibernatePersistenceProvider
                .createContainerEntityManagerFactory(persistenceUnitInfo, Collections.EMPTY_MAP);

        return entityManagerFactory.createEntityManager();
    }

}
