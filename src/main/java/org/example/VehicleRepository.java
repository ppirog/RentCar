package org.example;

interface VehicleRepository {

    void rentCar(final String registrationNumber, final String login);

    void returnCar(final String registrationNumber);

   Vehicle getVehicle(final String registrationNumber);

   boolean saveToCsv();


}
