package com.quangminh.hospitalmanagement.util;

import com.quangminh.hospitalmanagement.model.Doctor;
import com.quangminh.hospitalmanagement.model.Patient;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.sql.*;

public class DoctorUtil {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "root";
    private static ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    public static ObservableList<Doctor> getDoctors()
    {
        doctors.clear();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String sql = "select * from doctors";
            try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String speciality = resultSet.getString("specialty");

                    Doctor doctor = new Doctor(id,name,speciality);
                    doctors.add(doctor);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return doctors;
    }
    public static void addDoctor(String name,String specialty){
        String sql = "INSERT INTO doctors(name,specialty) VALUES(?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,specialty);

            int rowsInserted = preparedStatement.executeUpdate();
            if(rowsInserted>0){
                getDoctors();
            }else{
                System.out.println("Failed to insert doctor");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static TableColumn<Doctor,Integer> getIdColumn(){
        TableColumn<Doctor,Integer> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        return idColumn;
    }
    public static TableColumn<Doctor,String> getNameColumn(){
        TableColumn<Doctor,String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getName()));
        return  nameColumn;
    }

    public static TableColumn<Doctor,String> getSpecialtyColumn(){
        TableColumn<Doctor,String> specialtyColumn = new TableColumn<>("specialty");
        specialtyColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getSpecialty()));
        return specialtyColumn;
    }
}
