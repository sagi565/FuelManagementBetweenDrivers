package com.example.fuelmanagementbetweendrivers.Classes;

import java.util.Arrays;
import java.util.List;

public class Driver {
    private String name;
    private Car car;
    private List<Double> historyKilometersCounter;
    private Double currentKilometersCounter;

    private String driverID;



    public Driver(String name, Car car, List<Double> historyKilometersCounter, Double currentKilometersCounter, String driverID) {
        this.name = name;
        this.car = car;
        this.historyKilometersCounter = historyKilometersCounter;
        this.currentKilometersCounter = currentKilometersCounter;
        this.driverID = driverID;
    }
    public Driver(String name, String driverID) {
        this.name = name;
        this.driverID = driverID;
    }

    public Driver() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Double> getHistoryKilometersCounter() {
        return historyKilometersCounter;
    }

    public void setHistoryKilometersCounter(List<Double> historyKilometersCounter) {
        this.historyKilometersCounter = historyKilometersCounter;
    }

    public Double getCurrentKilometersCounter() {
        return currentKilometersCounter;
    }

    public void setCurrentKilometersCounter(Double currentKilometersCounter) {
        this.currentKilometersCounter = currentKilometersCounter;
    }
    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }
}
