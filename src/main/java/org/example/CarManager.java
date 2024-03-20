package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class CarManager implements VehicleRepository{

    private final String DB_PATH = "/home/ppirog/IdeaProjects/RentCar/src/main/resources/db.txt";
    private final File plikCSV = new File(DB_PATH);
    private final Map<String, Vehicle> vehicles = new HashMap<>();



    @Override
    public void rentCar(final String registrationNumber, final String user) {
        readCarFromCsv();
        Vehicle vehicle = vehicles.get(registrationNumber);
        if (vehicle != null && !vehicle.isRented()) {
            vehicle.setRented(true);
            vehicle.setRentedBy(user);
            vehicles.put(registrationNumber, vehicle);
            System.out.println("Samochód został wypożyczony");
        } else {
            System.out.println("Samochód o podanym numerze rejestracyjnym nie istnieje lub jest już wypożyczony");
        }
        saveToCsv();
    }

    @Override
    public void returnCar(final String registrationNumber) {
        readCarFromCsv();
        Vehicle vehicle = vehicles.get(registrationNumber);
        if (vehicle != null && vehicle.isRented()) {
            vehicle.setRented(false);
            vehicle.setRentedBy("-1");
            vehicles.put(registrationNumber, vehicle);
            System.out.println("Samochód został zwrócony");
        } else {
            System.out.println("Samochód o podanym numerze rejestracyjnym nie istnieje lub nie jest wypożyczony");
        }
        saveToCsv();
    }

    @Override
    public Vehicle getVehicle(final String registrationNumber) {
        return vehicles.get(registrationNumber);
    }

    public Vehicle addToDatabase(final Vehicle vehicle) {
        final Vehicle put = vehicles.put(vehicle.getRegistrationNumber(), vehicle);
        saveToCsv();
        return put;
    }

    @Override
    public boolean saveToCsv() {
        try (FileWriter writer = new FileWriter(plikCSV)) {
            for (Vehicle vehicle : vehicles.values()) {
                String line = vehicle.toCSV();
                writer.write(line);

            }
            return true;
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania danych do pliku CSV: " + e.getMessage());
            return false;
        }
    }

    private void readCarFromCsv() {

        try (Scanner scanner = new Scanner(plikCSV)) {
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                String[] dane = linia.split(",");
                Vehicle vehicle = VehicleFactory.createVehicle(dane);
                vehicles.put(vehicle.getRegistrationNumber(), vehicle);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Plik CSV nie został znaleziony: " + e.getMessage());
        }
    }






}
