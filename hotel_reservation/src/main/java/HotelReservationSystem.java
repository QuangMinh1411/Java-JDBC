import java.sql.*;
import java.util.Scanner;



public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try(
            Connection connection = DriverManager.getConnection(url,username,password);
            Scanner scanner = new Scanner(System.in)
        ){
            while(true){
                System.out.println();
                makeMenu();
                int choice = -1;
                if(scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                    continue;
                }
                switch(choice){
                    case 1:
                        reserveRoom(connection,scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,scanner);
                        break;
                    case 4:
                        updateReservation(connection,scanner);
                        break;
                    case 5:
                        deleteReservation(connection,scanner);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner) {
        try{
            System.out.print("Enter reservation ID to delete: ");
            int reservationID = scanner.nextInt();
            scanner.nextLine();
            if(!reservationExists(connection,reservationID)){
                System.out.println("Reservation ID not found.");
                return;
            }
            String sql = "DELETE FROM reservations WHERE reservation_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1, reservationID);
                int affectedRows = statement.executeUpdate();
                if(affectedRows>0){
                    System.out.println("Reservation deleted successfully.");
                }else {
                    System.out.println("Failed to delete reservation.");
                }
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner) {
        try{
            System.out.print("Enter reservation ID to update: ");
            int reservationID = scanner.nextInt();
            scanner.nextLine();
            if(!reservationExists(connection,reservationID)){
                System.out.println("Reservation ID not found.");
                return;
            }
            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.nextLine();
            String sql = "UPDATE reservations SET guest_name=?, room_number=?, contact_number=? WHERE reservation_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, newGuestName);
                statement.setInt(2, newRoomNumber);
                statement.setString(3, newContactNumber);
                statement.setInt(4, reservationID);
                int affectedRows = statement.executeUpdate();
                if(affectedRows>0){
                    System.out.println("Reservation updated successfully.");
                }else {
                    System.out.println("Failed to update reservation.");
                }
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    private static boolean reservationExists(Connection connection, int reservationID) {
        try{
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationID);
                try(ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try{
            System.out.print("Enter reservation ID: ");
            int reservationID = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();
            String sql = "SELECT room_number FROM reservations WHERE reservation_id = ? AND guest_name = ?";
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1, reservationID);
                statement.setString(2, guestName);
                try(ResultSet resultSet = statement.executeQuery()){
                    if(resultSet.next()){
                        int roomNumber = resultSet.getInt("room_number");
                        System.out.println("Room Number for Reservation ID: "+reservationID +
                                " and Guest Name: "+guestName+" is "+roomNumber);
                    }else {
                        System.out.println("No reservation found with the given ID and guest name.");
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private static void viewReservations(Connection connection) {
        String sql = "SELECT reservation_id,guest_name,room_number,contact_number,reservation_date FROM reservations";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            System.out.println("Current Reservations:");
            printTable();
            while(resultSet.next()){
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                Timestamp reservationDateTs = resultSet.getTimestamp("reservation_date");
                String reservationDate = reservationDateTs != null ? reservationDateTs.toString() : "";
                System.out.printf("|%-10d| %-13s | %-10d| %-10s | %-20s |\n",reservationId,guestName,roomNumber,contactNumber,reservationDate);
            }
            System.out.println("+----------+---------------+-----------+------------+-----------------------+");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection connection, Scanner scanner) {
        try{
            System.out.print("Enter customer name: ");
            String guestName = scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter contact number: ");
            String contactNumber = scanner.nextLine();
            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES (?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, guestName);
                statement.setInt(2, roomNumber);
                statement.setString(3, contactNumber);
                int affectedRows = statement.executeUpdate();
                if(affectedRows>0){
                    System.out.println("Reservation successful!");
                }else {
                    System.out.println("Reservation failed.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void makeMenu(){
        System.out.println("Hotel Management System");
        System.out.println("1. Reserve Room");
        System.out.println("2. View Reservations");
        System.out.println("3. Get Room Number");
        System.out.println("4. Update Reservation");
        System.out.println("5. Delete Reservation");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    private static void printTable(){
        System.out.println("+----------+---------------+-----------+------------+-----------------------+");
        System.out.println("| ID       | Name          | Room No.  | Contact    | Date                  |");
        System.out.println("+----------+---------------+-----------+------------+-----------------------+");
    }
}
