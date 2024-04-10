package org.example;

import java.util.Collection;

interface IVehicleRepository {

    boolean rentVehicle(String registrationNumber, String login);

    boolean returnVehicle(String plate);

    boolean addVehicle(Vehicle vehicle);

    boolean removeVehicle(String plate);

    Vehicle getVehicle(String plate);

    Collection<Vehicle> getVehicles();

}
