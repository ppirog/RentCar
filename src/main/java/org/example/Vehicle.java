package org.example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
abstract class Vehicle {

    protected final String registrationNumber;
    protected final String brand;
    protected final String model;
    protected final int year;

    @Setter
    protected int price;
    @Setter
    protected boolean isRented;

    Vehicle(final String registrationNumber, final String brand, final String model, final int year, final int price, final boolean isRented) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.isRented = isRented;
    }

    public String toCSV() {
        return this.getClass().getSimpleName() +","+ registrationNumber + "," + brand + "," + model + "," + year + "," + price + "," + isRented + "\n";
    }

}