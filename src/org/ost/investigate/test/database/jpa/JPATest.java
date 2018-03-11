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
        entityManager.close();
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test simple select from Derby")
    void testSimpleSelect(){

        Person person = new Person();
        person.setName("New Row");
        person.setEmail("test@gmail.com");
        person.setIdCode("1234567890");

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            ex.printStackTrace();
        }

            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT e FROM Person e");
            List<Person> persons = (List<Person>) query.getResultList();
            Assert.assertEquals(person, persons.get(0));
            entityManager.getTransaction().commit();
            
        entityManager.getTransaction().begin();
        entityManager.remove(person);
        entityManager.getTransaction().commit();
    }

    @Test
    @DisplayName("Test one to many for Derby")
    void testOneToMany(){
        Person person = new Person();
        person.setName("New Row");
        person.setEmail("test@gmail.com");
        person.setIdCode("1234567890");

        Phone phone = new Phone();
        phone.setNumber("0123456789");
        phone.setPerson(person);

        Phone phone1 = new Phone();
        phone.setNumber("1111111111");
        phone.setPerson(person);

        person.getPhones().add(phone);
        person.getPhones().add(phone1);

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.persist(phone);
            entityManager.persist(phone1);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            ex.printStackTrace();
        }


            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT e FROM Person e");
            List<Person> persons = (List<Person>) query.getResultList();
            Assert.assertEquals(person, persons.get(0));
            query = entityManager.createQuery("SELECT e FROM Phone e");
            List<Phone> phones = (List<Phone>) query.getResultList();
            Assert.assertTrue(phones.size() == 2);
            Assert.assertTrue(phones.contains(phone) && phones.contains(phone1));
            entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        entityManager.remove(person);
        entityManager.remove(phone);
        entityManager.remove(phone1);
        entityManager.getTransaction().commit();
    }
}
