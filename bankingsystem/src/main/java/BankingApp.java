import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long account_number;
            while(true){
                generalMenu();
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("*** Login Successful ***");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(scanner.nextInt()==1){
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account created successfully! Your Account Number is: " + account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number = accounts.open_account(email);
                            int choice2 = 0;
                            while(choice2!=5){
                                System.out.println();
                                System.out.println("1. Debit money");
                                System.out.println("2. Credit money");
                                System.out.println("3. Transfer money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Logout");
                                System.out.print("Choose an option:");
                                choice2 = scanner.nextInt();
                                switch (choice2){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                        default:
                                            System.out.println("Enter Valid Choice!");
                                            break;
                                }
                            }
                        }else{
                            System.out.println("Invalid Email or Password. Please try again.");
                        }
                    case 3:
                        System.out.println("Thank you for using Banking Application. Goodbye!");
                        return;
                        default:
                            System.out.println("Enter Valid Choice!");
                            break;

                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    private static void generalMenu(){
        System.out.println("*** Welcome to Banking Application ***");
        System.out.println("---------------------------------------");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option:");
    }
}
