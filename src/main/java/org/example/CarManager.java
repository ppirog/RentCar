package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class CarManager implements IVehicleRepository {

    private final String DB_PATH = "/home/ppirog/IdeaProjects/RentCar/src/main/resources/db.txt";
    private final File plikCSV = new File(DB_PATH);
    private final Map<String, Vehicle> vehicles = new HashMap<>();

    CarManager(){
        readCarFromCsv();
    }


    @Override
    public boolean rentVehicle(final String registrationNumber, final String user) {
        readCarFromCsv();
        Vehicle vehicle = vehicles.get(registrationNumber);
        boolean toRet = false;
        if (vehicle != null && !vehicle.isRented()) {
            vehicle.setRented(true);
            vehicle.setRentedBy(user);
            vehicles.put(registrationNumber, vehicle);
            System.out.println("Samochód został wypożyczony");
            toRet = true;
        } else {
            System.out.println("Samochód o podanym numerze rejestracyjnym nie istnieje lub jest już wypożyczony");
        }
        saveToCsv();
        return toRet;
    }

    @Override
    public boolean returnVehicle(final String plate) {
        readCarFromCsv();
        Vehicle vehicle = vehicles.get(plate);
        boolean toRet = false;
        if (vehicle != null && vehicle.isRented()) {

            vehicle.setRented(false);
            vehicle.setRentedBy("-1");
            vehicles.put(plate, vehicle);
            System.out.println("Samochód został zwrócony");
            toRet = true;
        } else {
            System.out.println("Samochód o podanym numerze rejestracyjnym nie istnieje lub nie jest wypożyczony");
        }
        saveToCsv();
        return toRet;
    }

    @Override
    public boolean addVehicle(final Vehicle vehicle) {

        final Vehicle put = vehicles.put(vehicle.getRegistrationNumber(), vehicle);
        saveToCsv();
        return true;

    }

    @Override
    public boolean removeVehicle(final String plate) {
        vehicles.remove(plate);
        saveToCsv();
        return true;
    }

    @Override
    public Vehicle getVehicle(final String plate) {
        return vehicles.get(plate);
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        return vehicles.values();
    }

    public Vehicle addToDatabase(final Vehicle vehicle) {
        final Vehicle put = vehicles.put(vehicle.getRegistrationNumber(), vehicle);
        saveToCsv();
        return put;
    }

    public void removeFromDatabase(final String registrationNumber) {
        vehicles.remove(registrationNumber);
        saveToCsv();
    }


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
