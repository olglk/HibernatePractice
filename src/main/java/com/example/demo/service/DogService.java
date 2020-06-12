package com.example.demo.service;

import com.example.demo.model.Dog;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.List;

@Repository
public class DogService {

    SessionFactory sessionFactory;

    DogService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public Dog createDog(Dog dog) {
        sessionFactory.getCurrentSession().persist(dog);
        return dog;
    }

    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    //@Lock(LockModeType.PESSIMISTIC_READ)
    //public List<Dog> getAllDogs() {
        //Dog dogTransient = new Dog(); // transient state of object
        //dogTransient.setId(1); //transient ---> detached (bc it has an ID) - not a good practice
        //Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        //Query query = session.createQuery("FROM Dog", Dog.class);
        //Dog dog = (Dog) query.getSingleResult(); // persist state of object
        //session.lock(dog, LockModeType.PESSIMISTIC_READ);
        //List<Dog> result = query.getResultList();
        //session.getTransaction().commit();
        //session.close();
        //dog.setName("Bobik"); // detached state object
        //return result;
    //}

    @Transactional
    public List<Dog> getAllDogs() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Dog", Dog.class)
                .getResultList();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Dog getDogById(int id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Dog d WHERE d.id = :id", Dog.class)
                .setParameter("id", id)
                .getSingleResult();
        //return sessionFactory.getCurrentSession().get(Dog.class, "1");
    }

    @Transactional
    public void deleteDogById(int id) {
        Dog dog = getDogById(id);
        sessionFactory.getCurrentSession()
                .remove(dog);
        dog.setName("Sharik"); // detached object
        sessionFactory.getCurrentSession().replicate(dog, ReplicationMode.LATEST_VERSION);
    }

    @Transactional
    public List<Dog> listDogOlderThan(int age) {
        return sessionFactory.getCurrentSession()
                .createNativeQuery("SELECT * FROM dog WHERE age > :age", Dog.class)
                .setParameter("age", age)
                .getResultList();
    }

    @Transactional
    public List<Dog> listDogOlderThanAverage() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT d FROM Dog d WHERE d.age > (SELECT AVG(d.age) FROM Dog d)", Dog.class)
                .getResultList();
    }
}