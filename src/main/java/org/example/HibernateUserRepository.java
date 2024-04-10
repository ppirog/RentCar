package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;
import java.util.List;

public class HibernateUserRepository implements IUserRepository {

    private final SessionFactory sessionFactory;

    public HibernateUserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addUser(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to add user", e);
        }
    }

    @Override
    public boolean exists(final String surname) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM tuser WHERE login = :login");
            query.setParameter("login", surname);
            User user = (User) query.uniqueResult();
            session.getTransaction().commit();
            return user != null;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to check if user exists", e);
        }

    }

    @Override
    public void removeUser(final String login) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM tuser WHERE login = :login");
            query.setParameter("login", login);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to remove user", e);
        }
    }

    @Override
    public User getUser(String login) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM tuser WHERE login = :login");
            query.setParameter("login", login);
            User user = (User) query.uniqueResult();
            session.getTransaction().commit();
            return user;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to get user", e);
        }
    }





    @Override
    public Collection<User> getUsers() {
        return List.of();
    }
}
