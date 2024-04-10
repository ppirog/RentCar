package org.example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;


@ToString
@Getter
@Entity
@Table(name = "tvehicle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "category")
abstract class Vehicle {
    @Id
    @Column(name = "registrationNumber")
    protected final String registrationNumber;
    protected final String brand;
    protected final String model;
    protected final int year;
    @Setter
    @Column(name = "rentedBy", columnDefinition = "VARCHAR")
    protected String rentedBy = "-1";
    @Setter
    protected int price;
    @Setter
    protected boolean isRented;

    Vehicle(final String registrationNumber, final String brand, final String model, final int year, final int price, final boolean isRented, final String rentedBy) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.isRented = isRented;
        this.rentedBy = rentedBy;
    }

    public Vehicle() {
        this.registrationNumber = null;
        this.brand = null;
        this.model = null;
        this.year = 0;
    }

    public String toCSV() {
        return this.getClass().getSimpleName() + "," + registrationNumber + "," + brand + "," + model + "," + year + "," + price + "," + isRented + "," + rentedBy + "\n";
    }

}
