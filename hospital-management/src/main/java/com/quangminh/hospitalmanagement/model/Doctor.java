package com.quangminh.hospitalmanagement.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Doctor {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private int id;
    private String name;
    private String specialty;

    public Doctor(int id, String name, String specialty) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
    }

    public Doctor(String specialty, String name) {
        this.id = counter.getAndIncrement();
        this.specialty = specialty;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
