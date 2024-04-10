package org.example;

import javax.persistence.Entity;

@Entity
class Car extends Vehicle {

    Car(final String registrationNumber, final String brand, final String model, final int year, final int price, final boolean isRented, final String rentedBy) {
        super(registrationNumber, brand, model, year, price, isRented, rentedBy);
    }

    public Car() {
        super();
    }
}
