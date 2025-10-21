package com.quangminh.bankingfxml.control;

import com.quangminh.bankingfxml.SceneFxmlApp;
import com.quangminh.bankingfxml.model.SceneName;
import com.quangminh.bankingfxml.model.Stageable;
import com.quangminh.bankingfxml.util.AccountUtil;
import com.quangminh.bankingfxml.util.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class NewAccountController implements Stageable {
    private Stage stage;
    private String email;

    @FXML
    private Label accountLbl;
    @FXML
    private Button openBtn;

    @FXML
    private VBox boxAccount;

    @FXML
    private Label lblName;

    @FXML
    private TextField txtName;

    @FXML
    private HBox lblAmount;

    @FXML
    private TextField txtAmount;

    @FXML
    private Label lblPin;

    @FXML
    private PasswordField txtPin;

    @FXML
    private Button exitBtn;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    public void initialize(){
        this.email = LoginController.success_email;
        accountLbl.setText("Create New Account for " + email);
        boxAccount.setVisible(false);
    }

    @FXML
    public void openNewAccount(ActionEvent event) {
        boxAccount.setVisible(true);
        exitBtn.setDisable(true);
    }

    @FXML
    public void openAccount(ActionEvent event) {
        String name = txtName.getText();
        String pin = txtPin.getText();
        double amount = Double.parseDouble(txtAmount.getText());
        LoginController.accountNumber = AccountUtil.open_account(email, name, amount, pin);
        LoginController.success_email = null;
        stage.setScene(SceneFxmlApp.getScenes().get(SceneName.TRANSACTION).getScene());
    }
}
