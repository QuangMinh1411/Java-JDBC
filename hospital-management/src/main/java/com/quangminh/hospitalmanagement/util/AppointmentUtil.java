package com.quangminh.hospitalmanagement.util;

import com.quangminh.hospitalmanagement.model.Appointment;
import com.quangminh.hospitalmanagement.model.Doctor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentUtil {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "root";
    private static ObservableList<Appointment> doctors = FXCollections.observableArrayList();
    public static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public static ObservableList<Appointment> getAppointments(){
        appointments.clear();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String sql = "select * from appointments";
            try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    int patient_id = resultSet.getInt("patient_id");
                    int doctor_id = resultSet.getInt("doctor_id");
                    LocalDate date = resultSet.getDate("appointment_date").toLocalDate();
                    Appointment appointment = new Appointment(id, patient_id, doctor_id, date);
                    appointments.add(appointment);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    public static void addAppointment(int patient_id,int doctor_id,LocalDate date){
        String sql = "INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,patient_id);
            preparedStatement.setInt(2,doctor_id);
            preparedStatement.setDate(3, Date.valueOf(date));
            int rowsInserted = preparedStatement.executeUpdate();
            if(rowsInserted>0){
                getAppointments();
            }else{
                System.out.println("Failed to insert doctor");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static TableColumn<Appointment,Integer> getIdColumn(){
        TableColumn<Appointment,Integer> idColumn = new TableColumn<>("Patient ID");
        idColumn.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getPatientId()).asObject());
        return idColumn;
    }
    public static TableColumn<Appointment,Integer> getDoctorIdColumn(){
        TableColumn<Appointment,Integer> doctorIdColumn = new TableColumn<>("Doctor ID");
        doctorIdColumn.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getDoctorId()).asObject());
        return doctorIdColumn;
    }
    public static TableColumn<Appointment, LocalDate> getAppointmentDateColumn() {
        TableColumn<Appointment, LocalDate> appointmentDateColumn = new TableColumn<>("Date");
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointment_date"));
        TableColumn<Appointment, LocalDate> format = appointmentDateColumn;
        format.setCellFactory(col -> {
            TableCell<Appointment, LocalDate> cell
                    = new TableCell<Appointment, LocalDate>() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    // Cleanup the cell before populating it
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        String formattedDob = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                                .format(item);
                        this.setText(formattedDob);
                    }
                }
            };
            return cell;
        });
        return format;
    }

    public static TableColumn<Appointment,String> getPatientName(){
        TableColumn<Appointment,String> patientNameColumn = new TableColumn<>("Patient Name");
        patientNameColumn.setCellValueFactory(
                cellData->{
                    Appointment appointment = cellData.getValue();
                    String patientName = PatientUtil.getPatientName(appointment.getPatientId());
                    return new SimpleStringProperty(patientName);
                }
        );
        return patientNameColumn;
    }

    public static TableColumn<Appointment,String> getDoctorName(){
        TableColumn<Appointment,String> doctorNameColumn = new TableColumn<>("Doctor Name");
        doctorNameColumn.setCellValueFactory(
          cellData->{
              Appointment appointment = cellData.getValue();
              String doctorName = DoctorUtil.getDoctorName(appointment.getDoctorId());
              return new SimpleStringProperty(doctorName);
          }
        );

        return doctorNameColumn;
    }



}

