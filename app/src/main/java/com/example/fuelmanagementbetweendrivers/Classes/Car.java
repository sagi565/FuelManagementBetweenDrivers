package com.example.fuelmanagementbetweendrivers.Classes;

import java.util.ArrayList;
import java.util.List;

public class Car {

    private String name;
    private String code;
    private List<String> drivers;
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Car(String name, String code, Driver driver) {
        this.name = name;
        this.code = code;
        drivers = new ArrayList<>();
        drivers.add(driver.getDocumentId());
    }
    public Car(String name, String code, String driverID) {
        this.name = name;
        this.code = code;
        drivers = new ArrayList<>();
        drivers.add(driverID);
    }
    public Car() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<String> drivers) {
        this.drivers = drivers;
    }
}
