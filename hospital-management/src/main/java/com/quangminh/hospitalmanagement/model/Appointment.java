package com.quangminh.hospitalmanagement.model;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Appointment {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDate appointmentDate;

    public Appointment(int patientId, int doctorId, LocalDate appointmentDate) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
