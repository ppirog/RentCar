package org.example;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Entity
class Motorcycle extends Vehicle {
    @Column(name = "category", insertable = false, updatable = false)
    private String category;


    Motorcycle(final String registrationNumber, final String brand, final String model, final int year, final int price, final boolean isRented,final String rentedBy, final String category) {
        super(registrationNumber, brand, model, year, price, isRented,rentedBy);
        this.category = category;

    }

    public Motorcycle() {
        super();
        this.category = null;
    }

    public String toCSV() {
        return this.getClass().getSimpleName() + "," + registrationNumber + "," + brand + "," + model + "," + year + "," + price + "," + isRented + "," + rentedBy + "," + category + "\n";
    }
}
