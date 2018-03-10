package org.ost.investigate.test.database.jpa;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ost.investigate.test.database.Tools;
import org.ost.investigate.test.database.jpa.entities.Phone;
import org.ost.investigate.test.database.jpa.entities.Person;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class JPATest {

    private static EntityManager entityManager;

    @BeforeEach
    void before(TestInfo testInfo) throws InterruptedException {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
        entityManager = Tools.createEntityManager(Tools.CONNECTION_BY_DEFAULT, Arrays.asList(Person.class.getName(), Phone.class.getName()));

    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        List<String> sqlList = Tools.PhoneSQLContainer.deleteIdCodeTable();
        sqlList.addAll(Tools.PersonSQLContainer.deletePersonTable());
        Tools.destroyJDBC(Tools.CONNECTION_BY_DEFAULT, sqlList);
        entityManager.close();
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

//    @Test
//    @DisplayName("Test delete only from Derby")
//    void testDeleteOnly() throws Exception {
//        List<String> sqlList = Tools.PersonSQLContainer.deletePersonTable();
//        sqlList.addAll(Tools.PhoneSQLContainer.deleteIdCodeTable());
//        Tools.destroyJDBC(Tools.CONNECTION_BY_DEFAULT, sqlList);
//    }

    @Test
    @DisplayName("Test simple select from Derby")
    void testSimpleSelect() throws Exception {
        try {
            entityManager.getTransaction().begin();
            Person p = new Person();
            p.setName("New Row");
            p.setEmail("test@gmail.com");
            p.setIdCode("1234567890");
            entityManager.persist(p);
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT e FROM Person e");
            List<Person> list = (List<Person>) query.getResultList();
            Assert.assertEquals(p, list.get(0));
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test one to one for Derby")
    void testOneToOne() throws Exception {
        try {
            entityManager.getTransaction().begin();

            Person p = new Person();
            p.setName("New Row");
            p.setEmail("test@gmail.com");
            p.setIdCode("1234567890");
            entityManager.persist(p);

            Phone iC = new Phone();
            iC.setNumber("0123456789");
            entityManager.persist(iC);

            entityManager.getTransaction().commit();

            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT e FROM Person e");
            List<Person> list = (List<Person>) query.getResultList();
            Assert.assertEquals(p, list.get(0));
            query = entityManager.createQuery("SELECT e FROM Phone e");
            List<Phone> listIC = (List<Phone>) query.getResultList();
            Assert.assertEquals(iC, listIC.get(0));
            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            ex.printStackTrace();
        }
    }
}
