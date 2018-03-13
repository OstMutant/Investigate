package org.ost.investigate.test.database.jpa;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ost.investigate.test.database.Tools;
import org.ost.investigate.test.database.jpa.dictionary.Gender;
import org.ost.investigate.test.database.jpa.dictionary.OperatorType;
import org.ost.investigate.test.database.jpa.entities.Community;
import org.ost.investigate.test.database.jpa.entities.Man;
import org.ost.investigate.test.database.jpa.entities.Mobile;
import org.ost.investigate.test.database.jpa.entities.Phone;
import org.ost.investigate.test.database.jpa.entities.Person;
import org.ost.investigate.test.database.jpa.entities.Woman;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JPATest {

    private static EntityManager entityManager;

    @BeforeEach
    void before(TestInfo testInfo) throws InterruptedException {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
        entityManager = Tools.createEntityManager(Tools.CONNECTION_BY_DEFAULT, Arrays.asList(Person.class.getName(), Phone.class.getName(), Community.class.getName(), Man.class.getName(), Woman.class.getName(), Mobile.class.getName()));

    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        entityManager.close();
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test simple select from Derby")
    void testSimpleSelect() {

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
    void testOneToMany() {
        Person person = new Person();
        person.setName("New Row");
        person.setEmail("test@gmail.com");
        person.setIdCode("1234567890");

        Phone phone = new Phone();
        phone.setNumber("0123456789");
        phone.setPerson(person);

        Phone phone1 = new Phone();
        phone1.setNumber("1111111111");
        phone1.setPerson(person);

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

    @Test
    @DisplayName("Test many to many for Derby")
    void testManyToMany() {
        Person person = new Person();
        person.setName("New Row");
        person.setEmail("test@gmail.com");
        person.setIdCode("1234567890");

        Person person1 = new Person();
        person1.setName("New Row1");
        person1.setEmail("test1@gmail.com");
        person1.setIdCode("1111111111");

        Community community = new Community();
        community.setName("Test");
        community.setDescription("Test Description");

        Community community1 = new Community();
        community1.setName("Test1");
        community1.setDescription("Test1 Description");

        person.getCommunities().add(community);
        person1.getCommunities().add(community);
        person1.getCommunities().add(community1);

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.persist(person1);
            entityManager.persist(community);
            entityManager.persist(community1);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            ex.printStackTrace();
        }


        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT e FROM Person e");
        List<Person> persons = (List<Person>) query.getResultList();
        Assert.assertTrue(persons.size() == 2);
        Assert.assertTrue(persons.contains(person) && persons.contains(person1));
        query = entityManager.createQuery("SELECT e FROM Community e");
        List<Community> communities = (List<Community>) query.getResultList();
        Assert.assertTrue(communities.size() == 2);
        Assert.assertTrue(communities.contains(community) && communities.contains(community1));
        query = entityManager.createQuery("SELECT e FROM Person e WHERE e.id=" + person.getId());
        Person personDB = (Person) query.getSingleResult();
        communities = personDB.getCommunities();
        Assert.assertTrue(communities.contains(community));
        query = entityManager.createQuery("SELECT e FROM Person e WHERE e.id=" + person1.getId());
        personDB = (Person) query.getSingleResult();
        communities = personDB.getCommunities();
        Assert.assertTrue(communities.contains(community) && communities.contains(community1));
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        entityManager.remove(person);
        entityManager.remove(person1);
        entityManager.remove(community);
        entityManager.remove(community1);
        entityManager.getTransaction().commit();
    }

    @Test
    @DisplayName("Test Single Table Inheritance for Derby")
    void testSingleTableInheritance() {
        Man man = new Man();
        man.setName("Artur");
        man.setEmail("test@gmail.com");
        man.setIdCode("1234567890");
        man.setGender(Gender.MALE);

        Woman woman = new Woman();
        woman.setName("Linda");
        woman.setEmail("test1@gmail.com");
        woman.setIdCode("1111111111");
        man.setGender(Gender.FEMALE);

        Community community = new Community();
        community.setName("Test");
        community.setDescription("Test Description");

        man.getCommunities().add(community);
        woman.getCommunities().add(community);

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(man);
            entityManager.persist(woman);
            entityManager.persist(community);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            ex.printStackTrace();
        }


        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT e FROM Man e");
        List<Person> persons = (List<Person>) query.getResultList();
        Assert.assertTrue(persons.size() == 1);
        Assert.assertTrue(persons.contains(man));
        query = entityManager.createQuery("SELECT e FROM Woman e");
        persons = (List<Person>) query.getResultList();
        Assert.assertTrue(persons.size() == 1);
        Assert.assertTrue(persons.contains(woman));
        query = entityManager.createQuery("SELECT e FROM Community e");
        List<Community> communities = (List<Community>) query.getResultList();
        Assert.assertTrue(communities.size() == 1);
        Assert.assertTrue(communities.contains(community));
        query = entityManager.createQuery("SELECT e FROM Person e WHERE e.id=" + man.getId());
        Person personDB = (Person) query.getSingleResult();
        communities = personDB.getCommunities();
        Assert.assertTrue(communities.contains(community));
        query = entityManager.createQuery("SELECT e FROM Person e WHERE e.id=" + woman.getId());
        personDB = (Person) query.getSingleResult();
        communities = personDB.getCommunities();
        Assert.assertTrue(communities.contains(community));
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        entityManager.remove(man);
        entityManager.remove(woman);
        entityManager.remove(community);
        entityManager.getTransaction().commit();
    }

    @Test
    @DisplayName("Test Joined Inheritance for Derby")
    void testJoinedInheritance() {
        Person person = new Person();
        person.setName("Artur");
        person.setEmail("test@gmail.com");
        person.setIdCode("1234567890");
        person.setGender(Gender.MALE);

        Phone phone = new Phone();
        phone.setNumber("0123456789");
        phone.setPerson(person);

        Mobile mobilePhone = new Mobile();
        mobilePhone.setDescription("Test Description");
        mobilePhone.setStart(new Date());
        mobilePhone.setNumber("1111111111");
        mobilePhone.setPerson(person);
        mobilePhone.setOperatorType(OperatorType.MOBILE);

        person.getPhones().add(phone);
        person.getPhones().add(mobilePhone);

        Community community = new Community();
        community.setName("Test");
        community.setDescription("Test Description");

        person.getCommunities().add(community);

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.persist(community);
            entityManager.persist(phone);
            entityManager.persist(mobilePhone);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            ex.printStackTrace();
        }


        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT e FROM Person e WHERE e.id=" + person.getId());
        Person personDB = (Person) query.getSingleResult();
        Assert.assertEquals(personDB, person);
        List<Phone> phones = personDB.getPhones();
        Assert.assertTrue(phones.contains(phone));
        Assert.assertTrue(phones.contains(mobilePhone));

        query = entityManager.createQuery("SELECT e FROM Mobile e");
        List<Mobile> mobiles = (List<Mobile>) query.getResultList();
        Assert.assertTrue(mobiles.contains(mobilePhone));
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        entityManager.remove(person);
        entityManager.remove(community);
        entityManager.remove(phone);
        entityManager.remove(mobilePhone);
        entityManager.getTransaction().commit();
    }
}
