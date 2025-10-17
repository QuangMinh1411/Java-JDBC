import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    private final String check_account_query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";

    public void credit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security pin:");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement creditStatement = connection.prepareStatement(credit_query);
                    creditStatement.setDouble(1, amount);
                    creditStatement.setLong(2, account_number);
                    int rowsAffected = creditStatement.executeUpdate();
                    if(rowsAffected > 0){
                        System.out.println("$" + amount + " credited successfully to account number: " + account_number);
                        connection.commit();
                        connection.setAutoCommit(true);
                    }else{
                        System.out.println("Credit failed. Please try again.");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else {
                    System.out.println("Invalid Security Pin. Transaction aborted.");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void debit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security pin:");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement(check_account_query);
                preparedStatement.setDouble(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query = "UPDATE accounts SET  balance = balance - ? WHERE account_number = ?";
                        PreparedStatement debitStatement = connection.prepareStatement(debit_query);
                        debitStatement.setDouble(1, amount);
                        debitStatement.setLong(2, account_number);
                        int rowsAffected = debitStatement.executeUpdate();
                        if(rowsAffected > 0){
                            System.out.println("$" + amount + " debited successfully from account number: ");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Debit failed. Please try again.");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("Insufficient balance. Transaction aborted.");
                    }
                }else{
                    System.out.println("Invalid Security Pin. Transaction aborted.");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter the receiver's account number:");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount to transfer:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter your security pin:");
        String security_pin = scanner.nextLine();
        try{
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
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement debitStatement = connection.prepareStatement(debit_query);
                        PreparedStatement creditStatement = connection.prepareStatement(credit_query);

                        creditStatement.setDouble(1, amount);
                        creditStatement.setLong(2, receiver_account_number);
                        debitStatement.setDouble(1, amount);
                        debitStatement.setLong(2, sender_account_number);
                        int rowsAffectedDebit = debitStatement.executeUpdate();
                        int rowsAffectedCredit = creditStatement.executeUpdate();
                        if(rowsAffectedDebit > 0 && rowsAffectedCredit > 0){
                            System.out.println("$" + amount + " transferred successfully from account number: " + sender_account_number + " to account number: " + receiver_account_number);
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transfer failed. Please try again.");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient balance. Transaction aborted.");
                    }
                }else{
                    System.out.println("Invalid Security Pin. Transaction aborted.");
                }
            }else{
                System.out.println("Invalid account numbers. Transaction aborted.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter security pin:");
        String security_pin = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(check_account_query);
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double current_balance = resultSet.getDouble("balance");
                System.out.println("Current balance for account number " + account_number + " is: $" + current_balance);
            }else {
                System.out.println("Invalid Security Pin. Transaction aborted.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


