package com.quangminh.hotelmanagement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.List;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HotelUtil {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "root";
    private static ObservableList<Hotel> hotels = FXCollections.observableArrayList();
    private static List<Integer> rooms = new ArrayList<>();
    public static ObservableList<Hotel> getHotelList() {
        hotels.clear(); // Clear previous entries to avoid duplicates
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT reservation_id,guest_name,room_number,contact_number,reservation_date FROM reservations";
            try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
                while(resultSet.next()){
                    int reservationId = resultSet.getInt("reservation_id");
                    String guestName = resultSet.getString("guest_name");
                    int roomNumber = resultSet.getInt("room_number");
                    String contactNumber = resultSet.getString("contact_number");
                    LocalTime reservationDate = resultSet.getTime("reservation_date").toLocalTime();
                    Hotel hotel = new Hotel(reservationId,guestName, roomNumber, contactNumber,reservationDate);
                    hotels.add(hotel);
                }
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return hotels;
    }
    public static TableColumn<Hotel,Integer> getIdColumn(){
        TableColumn<Hotel,Integer> idColumn = new TableColumn<>("Reservation ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getReservationId()).asObject());
        return idColumn;
    }
    public static TableColumn<Hotel,String> getGuestNameColumn(){
        TableColumn<Hotel,String> guestNameColumn = new TableColumn<>("Guest Name");
        guestNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuestName()));
        return guestNameColumn;
    }
    public static TableColumn<Hotel,Integer> getRoomNumberColumn(){
        TableColumn<Hotel,Integer> roomNumberColumn = new TableColumn<>("Room Number");
        roomNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRoomNumber()).asObject());
        return roomNumberColumn;
    }
    public static TableColumn<Hotel,String> getContactNumberColumn(){
        TableColumn<Hotel,String> contactNumberColumn = new TableColumn<>("Contact Number");
        contactNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContactNumber()));
        return contactNumberColumn;
    }
    public static TableColumn<Hotel, String> getReservationDateColumn(){
        TableColumn<Hotel,String> reservationDateColumn = new TableColumn<>("Reservation Date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        reservationDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReservationDate().format(formatter)));
        return reservationDateColumn;
    }

    public static void addReservation(String guestName, int roomNumber, String contactNumber) {
        String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, guestName);
            statement.setInt(2, roomNumber);
            statement.setString(3, contactNumber);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new reservation was inserted successfully!");
                // Refresh the hotel list to include the new reservation
                getHotelList();
            } else {
                System.out.println("Failed to insert the reservation.");
            }
        } catch (SQLException ex) {
            System.out.println("Error inserting reservation: " + ex.getMessage());
        }
    }
    public static void updateReservation(int reservationId, String guestName, int roomNumber, String contactNumber) {
        String sql = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, guestName);
            statement.setInt(2, roomNumber);
            statement.setString(3, contactNumber);
            statement.setInt(4, reservationId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("The reservation was updated successfully!");
                // Refresh the hotel list to reflect the updated reservation
                getHotelList();
            } else {
                System.out.println("Failed to update the reservation.");
            }
        } catch (SQLException ex) {
            System.out.println("Error updating reservation: " + ex.getMessage());
        }


    }
    public static boolean reservationExists(int reservationId) {
        String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException ex) {
            System.out.println("Error checking reservation existence: " + ex.getMessage());
            return false;
        }
    }
    public static List<Integer> getRoomNumber(String guestName) {
        rooms.clear();
        String sql = "SELECT room_number FROM reservations WHERE guest_name = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, guestName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    rooms.add(roomNumber);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error retrieving room number: " + ex.getMessage());
        }
        return rooms.size()==0 ? null : rooms;
    }
    public static void deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservationId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                // Refresh the hotel list to reflect the deletion
                getHotelList();
            } else {
                System.out.println("Failed to delete the reservation.");
            }
        } catch (SQLException ex) {
            System.out.println("Error deleting reservation: " + ex.getMessage());
        }
    }
}
