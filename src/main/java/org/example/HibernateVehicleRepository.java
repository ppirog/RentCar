package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class HibernateVehicleRepository implements IVehicleRepository {

    private final SessionFactory sessionFactory;

    public HibernateVehicleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean rentVehicle(String registrationNumber, String login) {
        // Tworzenie sesji
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery("UPDATE Vehicle SET isRented = true, rentedBy = :login WHERE registrationNumber = :registrationNumber AND isRented = false");
            query.setParameter("login", login);
            query.setParameter("registrationNumber", registrationNumber);
            int updatedRows = query.executeUpdate();

            session.getTransaction().commit();
            return updatedRows > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to rent vehicle", e);
        }
    }

    @Override
    public boolean returnVehicle(String plate) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery("UPDATE Vehicle SET isRented = false, rentedBy = '' WHERE registrationNumber = :plate AND isRented = true");
            query.setParameter("plate", plate);
            int updatedRows = query.executeUpdate();

            session.getTransaction().commit();
            return updatedRows > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to return vehicle", e);
        }
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.save(vehicle);

            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to add vehicle", e);
        }
    }

    @Override
    public boolean removeVehicle(String plate) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery("DELETE FROM Vehicle WHERE registrationNumber = :plate");
            query.setParameter("plate", plate);
            int updatedRows = query.executeUpdate();

            session.getTransaction().commit();
            return updatedRows > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to remove vehicle", e);
        }
    }

    @Override
    public Vehicle getVehicle(String plate) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery("FROM Vehicle WHERE registrationNumber = :plate");
            query.setParameter("plate", plate);
            Vehicle vehicle = (Vehicle) query.uniqueResult();

            session.getTransaction().commit();
            return vehicle;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to get vehicle", e);
        }
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery("FROM Vehicle");
            Collection<Vehicle> vehicles = query.list();

            session.getTransaction().commit();
            return vehicles;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to get vehicles", e);
        }
    }
}
