package com.quangminh.hotelmanagement;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Hotel {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private int reservationId;
    private String guestName;
    private int roomNumber;
    private String contactNumber;
    private LocalTime reservationDate;

    public Hotel(String guestName, int roomNumber, String contactNumber) {
        this.reservationId = counter.getAndIncrement();
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.contactNumber = contactNumber;
        this.reservationDate = LocalTime.now();
    }

    public Hotel(String guestName, int roomNumber, String contactNumber, LocalTime reservationDate) {
        this.reservationId = counter.getAndIncrement();
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.contactNumber = contactNumber;
        this.reservationDate = reservationDate;
    }

    public Hotel(int reservationId, String guestName, int roomNumber, String contactNumber,LocalTime reservationDate) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.contactNumber = contactNumber;
        this.reservationDate = reservationDate;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getReservationId() {
        return reservationId;
    }
}
