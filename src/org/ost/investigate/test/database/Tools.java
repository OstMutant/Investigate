package org.ost.investigate.test.database;

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
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Tools {

    public final String CONST_URL = "jdbc:derby:memory:testdb;create=true";
    public final String CONST_USER = "test";
    public final String CONST_PASSWORD = "pass123";

    public interface ConnectionDataGetter {

        String getUrl();

        String getUser();

        String getPassword();
    }

    public static class ConnectionBean implements ConnectionDataGetter {
        //        public final String CONST_URL = "jdbc:derby:testdb;create=true";
        public final String CONST_URL = "jdbc:derby:memory:testdb;create=true";
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

    public static final ConnectionBean CONNECTION_BY_DEFAULT = new ConnectionBean();

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
