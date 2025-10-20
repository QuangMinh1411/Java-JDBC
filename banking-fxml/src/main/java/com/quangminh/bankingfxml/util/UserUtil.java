package com.quangminh.bankingfxml.util;

import com.quangminh.bankingfxml.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.sql.*;

public class UserUtil {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "root";
    private static ObservableList<User> users = FXCollections.observableArrayList();

    public static ObservableList<User> getUsers() {
        users.clear();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT * FROM user";
            try(Statement statement = connection.createStatement()){
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    String fullname = resultSet.getString("full_name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    User user = new User(fullname,email,password);
                    users.add(user);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return users;
    }

    public static TableColumn<User,String> getColumnByName(){
        TableColumn<User,String> nameColumn = new TableColumn<>("Full Name");
        nameColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getFullname()));
        return nameColumn;
    }
    public static TableColumn<User,String> getColumnEmail(){
        TableColumn<User,String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getEmail()));
        return emailColumn;
    }
    public static TableColumn<User,String> getColumnPassword(){
        TableColumn<User,String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getPassword()));
        return passwordColumn;
    }

    public static void register(String fullname, String email, String pass){
        if(user_exist(email)){
            System.out.println("User already exists with this email");
            return;
        }
        String register_query = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = conn.prepareStatement(register_query)) {
            preparedStatement.setString(1, fullname);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, pass);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Registration successful!");
                getUsers();

            }else {
                System.out.println("Registration failed. Please try again.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    public static boolean user_exist(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try(Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            try(ResultSet resultSet = statement.executeQuery()){
                return  (resultSet.next());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static String login(String email,String pass){
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try(Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = conn.prepareStatement(login_query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()) {
                    return email;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
