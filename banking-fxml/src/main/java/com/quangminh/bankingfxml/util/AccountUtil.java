package com.quangminh.bankingfxml.util;

import com.quangminh.bankingfxml.model.Accounts;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.sql.*;

public class AccountUtil {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "root";
    private static ObservableList<Accounts> accounts = FXCollections.observableArrayList();

    public static ObservableList<Accounts> getAccounts() {
        accounts.clear();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT * FROM accounts";
            try(Statement statement = connection.createStatement()){
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    long account_number = resultSet.getLong("account_number");
                    String full_name = resultSet.getString("full_name");
                    String email = resultSet.getString("email");
                    double balance = resultSet.getDouble("balance");
                    String security_pin = resultSet.getString("security_pin");
                    Accounts account = new Accounts(account_number,full_name,email,balance,security_pin);
                    accounts.add(account);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public static long open_account(String email, String full_name, double initial_amount, String security_pin){
        if(!account_exist(email)){
            String open_account_query = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            try{
                long account_number = generateAccountNumber();
                try(Connection connection = DriverManager.getConnection(url,username,password)){
                    PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                    preparedStatement.setLong(1, account_number);
                    preparedStatement.setString(2, full_name);
                    preparedStatement.setString(3, email);
                    preparedStatement.setDouble(4, initial_amount);
                    preparedStatement.setString(5, security_pin);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected > 0){
                        getAccounts();
                        return account_number;
                    }else{
                        throw new RuntimeException("Account creation failed. Please try again.");
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        else {
            String sql = "SELECT account_number FROM accounts WHERE email = ?";
            try(Connection connection = DriverManager.getConnection(url,username,password)){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong("account_number");
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account creation failed. Please try again.");
    }


    private static long generateAccountNumber() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from accounts ORDER BY account_number DESC LIMIT 1");
            if(resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            }else{
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }


    public static boolean account_exist(String email){
        String query = "SELECT * FROM accounts WHERE email = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static TableColumn<Accounts,Long> getColumnAccountNumber(){
        TableColumn<Accounts,Long> accountNumberColumn = new TableColumn<>("Account Number");
        accountNumberColumn.setCellValueFactory(cellData->new SimpleLongProperty(cellData.getValue().getAccount_number()).asObject());
        return accountNumberColumn;
    }
    public static TableColumn<Accounts,String> getColumnFullName() {
        TableColumn<Accounts, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFull_name()));
        return fullNameColumn;
    }
    public static TableColumn<Accounts,String> getColumnEmail(){
        TableColumn<Accounts,String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getEmail()));
        return emailColumn;
    }
    public static TableColumn<Accounts,Double> getColumnBalance(){
        TableColumn<Accounts,Double> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(cellData->new SimpleDoubleProperty(cellData.getValue().getBalance()).asObject());
        return balanceColumn;
    }
    public static TableColumn<Accounts,String> getColumnSecurityPin(){
        TableColumn<Accounts,String> securityPinColumn = new TableColumn<>("Security PIN");
        securityPinColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getSecurity_pin()));
        return securityPinColumn;
    }

    public static long getAccountNumberByEmail(String email){
        String sql = "SELECT account_number FROM accounts WHERE email = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }

        // return -1 if not found or on error
        return -1;
    }

    public static int debit_money(long account_number,double amount,String sercurity_pin){
        String check_account_query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement(check_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, sercurity_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement debitStatement = connection.prepareStatement(debit_query);
                        debitStatement.setDouble(1, amount);
                        debitStatement.setLong(2, account_number);
                        int rowsAffected = debitStatement.executeUpdate();
                        if(rowsAffected>0){
                            connection.commit();
                            getAccounts();
                            connection.setAutoCommit(true);
                            return 1;
                        }else{
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient balance. Transaction aborted.");
                        return 0;
                    }
                }
                else {
                    System.out.println("Invalid Security Pin. Transaction aborted.");
                    return -1;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return -2;
    }

    public static int credit_money(long account_number,double amount,String sercurity_pin){
        String check_account_query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement(check_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, sercurity_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement creditStatement = connection.prepareStatement(credit_query);
                    creditStatement.setDouble(1, amount);
                    creditStatement.setLong(2, account_number);
                    int rowsAffected = creditStatement.executeUpdate();
                    if(rowsAffected > 0){
                        connection.commit();
                        getAccounts();
                        connection.setAutoCommit(true);
                        return 1;
                    }else{
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else {
                    System.out.println("Invalid Security Pin. Transaction aborted.");
                    return -1;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return -2;
    }

    public static int transfer_money(long sender_account_number, long receiver_account_number, double amount, String security_pin){
        String check_account_query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement(check_account_query);
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement debitStatement = connection.prepareStatement(debit_query);
                        debitStatement.setDouble(1, amount);
                        debitStatement.setLong(2, sender_account_number);
                        int rowsAffected1 = debitStatement.executeUpdate();

                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement creditStatement = connection.prepareStatement(credit_query);
                        creditStatement.setDouble(1, amount);
                        creditStatement.setLong(2, receiver_account_number);
                        int rowsAffected2 = creditStatement.executeUpdate();

                        if(rowsAffected1>0 && rowsAffected2>0){
                            connection.commit();
                            getAccounts();
                            connection.setAutoCommit(true);
                            return 1;
                        }else{
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient balance. Transaction aborted.");
                        return 0;
                    }
                }
                else {
                    System.out.println("Invalid Security Pin. Transaction aborted.");
                    return -1;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return -2;
    }
    public static double getBalance(long account_number, String security_pin) {
        String check_account_query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            PreparedStatement preparedStatement = connection.prepareStatement(check_account_query);
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double current_balance = resultSet.getDouble("balance");
                return current_balance;
            }else{
                System.out.println("Invalid Security Pin.");
                return -1;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return -2;
    }

}
