package com.quangminh.hotelmanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HotelController {
    @FXML
    private Button btnDelete;
    @FXML
    private VBox roomInfor;
    @FXML
    private Button btnFind;
    @FXML
    private TextField txtGuestName;
    @FXML
    private Label welcomeText;
    @FXML
    private BorderPane hotelPane;

    private TableView<Hotel> hotelTable;

    @FXML
    private VBox bookPane;
    @FXML
    private Label lblName;
    @FXML
    private TextField txtName;
    @FXML
    private Label lblRoom;
    @FXML
    private TextField txtRoom;
    @FXML
    private Label lblContact;
    @FXML
    private TextField txtContact;
    @FXML
    private Button btnAdd;
    @FXML
    private TextField txtId;

    @FXML
    private Button btnUpdate;
    @FXML
    private Label msgLbl;

    @FXML
    public void initialize() {
        hotelTable = new TableView<>(HotelUtil.getHotelList());
        hotelTable.getColumns().addAll(HotelUtil.getIdColumn(),
                                       HotelUtil.getGuestNameColumn(),
                                       HotelUtil.getRoomNumberColumn(),
                                       HotelUtil.getContactNumberColumn(),
                                        HotelUtil.getReservationDateColumn());
        hotelPane.setCenter(hotelTable);

    }

    @FXML
    public void book(ActionEvent event) {
        msgLbl.setText("");
        String name = txtName.getText();
        int roomNumber;
        try {
            roomNumber = Integer.parseInt(txtRoom.getText());
        } catch (NumberFormatException e) {
            msgLbl.setText("Invalid room number. Please enter a valid integer.");
            return;
        }
        String contact = txtContact.getText();

        if (name.isEmpty() || contact.isEmpty()) {
            msgLbl.setText("Please fill in all fields.");
            return;
        }
        HotelUtil.addReservation(name, roomNumber, contact);
        msgLbl.setText("Reservation added successfully.");
        refreshTable();
        clearInputFields();
    }


    public void refreshTable(){
        hotelTable.setItems(HotelUtil.getHotelList());
    }

    @FXML
    public void update(ActionEvent event) {
        msgLbl.setText("");
        try{
            int id = Integer.parseInt(txtId.getText());
            if(!HotelUtil.reservationExists(id)){
                msgLbl.setText("Reservation ID "+id+" not found.");
                return;
            }
            String name = txtName.getText();
            int roomNumber = Integer.parseInt(txtRoom.getText());
            String contact = txtContact.getText();
            if(name.isEmpty() || contact.isEmpty()){
                msgLbl.setText("Please fill in all fields.");
                return;
            }
            HotelUtil.updateReservation(id,name,roomNumber,contact);
            refreshTable();
            msgLbl.setText("Reservation updated successfully.");
        }catch(NumberFormatException e){
            msgLbl.setText("Invalid reservation ID or room number.");
            return;
        }
        clearInputFields();

    }

    @FXML
    public void find(ActionEvent event) {
        msgLbl.setText("");
        roomInfor.getChildren().clear();
        String guestName = txtGuestName.getText();
        var list = HotelUtil.getRoomNumber(guestName);
        System.out.println(list);
        if(list == null || list.isEmpty()){
                msgLbl.setText("No reservations found for guest: "+guestName);
        }else{
            list.stream().forEach((roomNumber) -> {
                String msg = guestName+" has booked room number: "+roomNumber;
                Label label = new Label(msg);
                label.setWrapText(true);
                roomInfor.getChildren().add(label);
            });
        }

    }

    @FXML
    public void delete(ActionEvent event) {
        msgLbl.setText("");
        try{
            int id = Integer.parseInt(txtId.getText());
            if(!HotelUtil.reservationExists(id)){
                msgLbl.setText("Reservation ID "+id+" not found.");
                return;
            }

            HotelUtil.deleteReservation(id);
            refreshTable();

            msgLbl.setText("Reservation ID: "+id+" deleted successfully.");
        }catch(NumberFormatException e){
            msgLbl.setText("Invalid reservation ID ");
            return;
        }
        clearInputFields();

    }

    private void clearInputFields() {
        txtId.clear();
        txtName.clear();
        txtRoom.clear();
        txtContact.clear();
    }
}
