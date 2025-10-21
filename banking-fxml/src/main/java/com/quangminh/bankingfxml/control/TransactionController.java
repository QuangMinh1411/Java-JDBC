package com.quangminh.bankingfxml.control;

import com.quangminh.bankingfxml.SceneFxmlApp;
import com.quangminh.bankingfxml.model.SceneName;
import com.quangminh.bankingfxml.model.Stageable;
import com.quangminh.bankingfxml.util.AccountUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class TransactionController implements Stageable {
    private Stage stage;
    @FXML
    private TextField secureTxt;
    @FXML
    private Label lblAccount;

    @FXML
    private HBox boxDebit;

    @FXML
    private TextField txtDebit;

    @FXML
    private Button debitBtn;

    @FXML
    private HBox boxCredit;

    @FXML
    private TextField txtCredit;

    @FXML
    private Button creditBtn;

    @FXML
    private HBox boxTransfer;

    @FXML
    private TextField txtReceiverNum;
    @FXML
    private TextField txtAmount;

    @FXML
    private Button transferBtn;

    @FXML
    private HBox boxBalance;

    @FXML
    private Button balanceBtn;

    @FXML
    private TextField txtBalance;
    @FXML
    public void initialize(){
        lblAccount.setText("All Transactions for Account Number: " + LoginController.accountNumber);
    }
    @FXML
    public void logout(ActionEvent event) {
        resetForm();
        Platform.exit();
    }


    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void resetForm(){
        txtDebit.clear();
        txtCredit.clear();
        txtReceiverNum.clear();
        txtBalance.clear();
        secureTxt.clear();
    }

    @FXML
    public void debit(ActionEvent event) {
        double amount = Double.parseDouble(txtDebit.getText());
        String security_pin = secureTxt.getText();
        int res = AccountUtil.debit_money(LoginController.accountNumber, amount, security_pin);
        Window owner = debitBtn.getScene().getWindow();
        showAlert(res, "You have successfully debited $" + amount + " from your account.");
        resetForm();
    }

    @FXML
    public void credit(ActionEvent event) {
        double amount = Double.parseDouble(txtCredit.getText());
        String security_pin = secureTxt.getText();
        int res = AccountUtil.credit_money(LoginController.accountNumber, amount, security_pin);
        Window owner = creditBtn.getScene().getWindow();
        showAlert(res, "You have successfully credited $" + amount + " to your account.");
        resetForm();
    }
    @FXML
    public void transfer(ActionEvent event) {
        double amount = Double.parseDouble(txtAmount.getText());
        String security_pin = secureTxt.getText();
        long receiver_account_number = Long.parseLong(txtReceiverNum.getText());
        int res = AccountUtil.transfer_money(LoginController.accountNumber, receiver_account_number, amount, security_pin);
        Window owner = transferBtn.getScene().getWindow();
        showAlert(res, "You have successfully transferred $" + amount + " to account number: " + receiver_account_number);
        resetForm();
    }
    @FXML
    public void checkBalance(ActionEvent event) {
        String security_pin = secureTxt.getText();
        double balance = AccountUtil.getBalance(LoginController.accountNumber, security_pin);
        Window owner = balanceBtn.getScene().getWindow();
        if(balance<0){
            showAlert(-1, "Invalid Security Pin. Transaction aborted.");
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Balance Information");
            alert.setHeaderText(null);
            alert.setContentText("Your current balance is: $" + balance);
            alert.initOwner(owner);
            alert.showAndWait();
        }
        resetForm();
    }

    private void showAlert(int res,String msg){
        Window owner = stage.getScene().getWindow();
        if(res<0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Security Pin. Transaction aborted.");
            alert.initOwner(owner);
            alert.showAndWait();
            return;
        }else if(res==0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Transaction Failed");
            alert.setHeaderText(null);
            alert.setContentText("Insufficient balance. Transaction aborted.");
            alert.initOwner(owner);
            alert.showAndWait();
            return;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Transaction Successful");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.initOwner(owner);
            alert.showAndWait();
        }
    }


}
