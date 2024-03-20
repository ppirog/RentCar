package org.example;

class VehicleFactory {

    public static Vehicle createVehicle(final String[] vehicleData) {
        return switch (vehicleData[0]) {
            case "Car" ->
                    new Car(vehicleData[1], vehicleData[2], vehicleData[3], Integer.parseInt(vehicleData[4]), Integer.parseInt(vehicleData[5]), Boolean.parseBoolean(vehicleData[6]), vehicleData[7]);
            case "Motorcycle" ->
                    new Motorcycle(vehicleData[1], vehicleData[2], vehicleData[3], Integer.parseInt(vehicleData[4]), Integer.parseInt(vehicleData[5]), Boolean.parseBoolean(vehicleData[6]), vehicleData[7], vehicleData[8]);
            default -> throw new IllegalStateException("Unexpected value: " + vehicleData[0]);
        };
    }
}
