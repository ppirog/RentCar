package org.example;

interface VehicleRepository {

    void rentCar(final String registrationNumber);

    void returnCar(final String registrationNumber);

   Vehicle getVehicle(final String registrationNumber);

   boolean saveToCsv();


}
