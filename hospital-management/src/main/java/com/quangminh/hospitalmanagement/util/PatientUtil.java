package com.quangminh.hospitalmanagement.util;

import com.quangminh.hospitalmanagement.model.Patient;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.sql.*;

public class PatientUtil {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "root";
    private static ObservableList<Patient> patients = FXCollections.observableArrayList();
    public static ObservableList<Patient> getPatients() {
        patients.clear();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String sql = "select * from patients";
            try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    String gender = resultSet.getString("gender");
                    Patient patient = new Patient(id,name,gender,age);
                    patients.add(patient);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return patients;
    }
    public static void addPatient(String name,String gender,int age){
        String sql = "insert into patients(name,gender,age) values(?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,gender);
            preparedStatement.setInt(3,age);
            int rowsInserted = preparedStatement.executeUpdate();
            if(rowsInserted>0){
                getPatients();
            }else{
                System.out.println("Failed to insert patient");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static TableColumn<Patient,Integer> getIdColumn(){
        TableColumn<Patient,Integer> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        return idColumn;
    }
    public static TableColumn<Patient,String> getNameColumn(){
        TableColumn<Patient,String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getName()));
        return  nameColumn;
    }
    public static TableColumn<Patient,Integer> getAgeColumn(){
        TableColumn<Patient,Integer> ageColumn = new TableColumn<>("age");
        ageColumn.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
        return ageColumn;
    }
    public static TableColumn<Patient,String> getGenderColumn(){
        TableColumn<Patient,String> genderColumn = new TableColumn<>("gender");
        genderColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getGender()));
        return genderColumn;
    }
}
