package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcVehicleRepository implements IVehicleRepository {
    private static JdbcVehicleRepository instance;
    private final DatabaseManager manager;

    public JdbcVehicleRepository() {
        this.manager = DatabaseManager.getInstance();
    }

    public static JdbcVehicleRepository getInstance() {
        if (JdbcVehicleRepository.instance == null) {
            instance = new JdbcVehicleRepository();
        }
        return instance;
    }

    @Override
    public boolean rentVehicle(final String registrationNumber, final String login) {

        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;

        try {
            conn = manager.getConnection();
            conn.createStatement();
            //najpierw sprawdzamy czy pojazd jest dostępny
            rentCarStmt = conn.prepareStatement("SELECT * FROM tvehicle WHERE registrationNumber LIKE ? AND isrented = false");

            rentCarStmt.setString(1, registrationNumber);
            ResultSet resultSet = rentCarStmt.executeQuery();


            if (resultSet.next()) {

                updateUserStmt = conn.prepareStatement("UPDATE tvehicle SET isrented = true, rentedBy = ? WHERE registrationNumber LIKE ? AND isrented = false");
                updateUserStmt.setString(1, login);
                updateUserStmt.setString(2, registrationNumber);
                int updatedRows = updateUserStmt.executeUpdate();
                System.out.println("Pojazd został wypożyczony");
            } else {
                System.out.println("Pojazd jest już wypożyczony");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return false;
    }

    @Override
    public boolean returnVehicle(final String plate) {

        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;

        try {
            conn = manager.getConnection();
            conn.createStatement();
            //najpierw sprawdzamy czy pojazd jest dostępny
            rentCarStmt = conn.prepareStatement("SELECT * FROM tvehicle WHERE registrationNumber LIKE ? AND isrented = true");

            rentCarStmt.setString(1, plate);
            ResultSet resultSet = rentCarStmt.executeQuery();

            if (resultSet.next()) {
                updateUserStmt = conn.prepareStatement("UPDATE tvehicle SET isrented = false, rentedBy = ? WHERE registrationNumber LIKE ? AND isrented = true");
                updateUserStmt.setString(1, "");
                updateUserStmt.setString(2, plate);
                int updatedRows = updateUserStmt.executeUpdate();
                System.out.println("Pojazd został zwrócony");
            } else {
                System.out.println("Nie ma takiego pojazdu wypożyczonego");
            }

            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addVehicle(final Vehicle vehicle) {

        Connection conn = null;
        PreparedStatement addVehicleStmt = null;

        try {
            conn = manager.getConnection();
            conn.createStatement();
            addVehicleStmt = conn.prepareStatement("INSERT INTO tvehicle (registrationNumber, brand, model, year, price, isrented, rentedBy, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            addVehicleStmt.setString(1, vehicle.getRegistrationNumber());
            addVehicleStmt.setString(2, vehicle.getBrand());
            addVehicleStmt.setString(3, vehicle.getModel());
            addVehicleStmt.setInt(4, vehicle.getYear());
            addVehicleStmt.setInt(5, vehicle.getPrice());
            addVehicleStmt.setBoolean(6, vehicle.isRented());
            addVehicleStmt.setString(7, vehicle.getRentedBy());

            String category = vehicle.getClass().getSimpleName().equals("Motorcycle") ? ((Motorcycle) vehicle).getCategory() : "";
            addVehicleStmt.setString(8, category);
            int updatedRows = addVehicleStmt.executeUpdate();
            System.out.println("Pojazd został dodany");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean removeVehicle(final String plate) {

        Connection conn = null;
        PreparedStatement removeVehicleStmt = null;

        try {
            conn = manager.getConnection();
            conn.createStatement();
            removeVehicleStmt = conn.prepareStatement("DELETE FROM tvehicle WHERE registrationNumber LIKE ?");
            removeVehicleStmt.setString(1, plate);
            int updatedRows = removeVehicleStmt.executeUpdate();
            System.out.println("Pojazd został usunięty");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public Vehicle getVehicle(final String plate) {
        Connection conn = null;
        PreparedStatement getVehicleStmt = null;

        try {
            conn = manager.getConnection();
            conn.createStatement();
            getVehicleStmt = conn.prepareStatement("SELECT * FROM tvehicle WHERE registrationNumber LIKE ?");
            getVehicleStmt.setString(1, plate);
            ResultSet resultSet = getVehicleStmt.executeQuery();

            if (resultSet.next()) {
                String category = resultSet.getString("category");
                String registrationNumber = resultSet.getString("registrationNumber");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                int year = resultSet.getInt("year");
                int price = resultSet.getInt("price");
                boolean isRented = resultSet.getBoolean("isrented");
                String rentedBy = resultSet.getString("rentedBy");

                if (category.equals("Motorcycle")) {
                    return new Motorcycle(registrationNumber, brand, model, year, price, isRented, rentedBy, category);
                } else {
                    return new Car(registrationNumber, brand, model, year, price, isRented, rentedBy);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Collection<Vehicle> getVehicles() {

        Connection conn = null;
        PreparedStatement getVehiclesStmt = null;

        try {
            conn = manager.getConnection();
            conn.createStatement();
            getVehiclesStmt = conn.prepareStatement("SELECT * FROM tvehicle");
            ResultSet resultSet = getVehiclesStmt.executeQuery();


            Collection<Vehicle> vehicles = new ArrayList<>();


            while (resultSet.next()) {
                String category = resultSet.getString("category");
                String registrationNumber = resultSet.getString("registrationNumber");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                int year = resultSet.getInt("year");
                int price = resultSet.getInt("price");
                boolean isRented = resultSet.getBoolean("isrented");
                String rentedBy = resultSet.getString("rentedBy");

                if (category.equals("Motorcycle")) {
                    vehicles.add(new Motorcycle(registrationNumber, brand, model, year, price, isRented, rentedBy, category));
                } else {
                    vehicles.add(new Car(registrationNumber, brand, model, year, price, isRented, rentedBy));
                }
            }

            return vehicles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
