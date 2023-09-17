package com.example.fuelmanagementbetweendrivers.Classes;

import java.util.List;

public class Car {
    private List<Driver> drivers;

    public Car(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }
}
