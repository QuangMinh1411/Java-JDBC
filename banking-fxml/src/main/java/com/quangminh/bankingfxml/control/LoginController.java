package com.quangminh.bankingfxml.control;

import com.quangminh.bankingfxml.model.Stageable;
import com.quangminh.bankingfxml.util.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Stageable {
    private Stage stage;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button logBtn;



    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void logIn(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String login_email = UserUtil.login(email, password);


    }
}
