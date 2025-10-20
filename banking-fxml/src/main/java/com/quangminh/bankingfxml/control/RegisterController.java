package com.quangminh.bankingfxml.control;

import com.quangminh.bankingfxml.SceneFxmlApp;
import com.quangminh.bankingfxml.model.SceneName;
import com.quangminh.bankingfxml.model.Stageable;
import com.quangminh.bankingfxml.util.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController implements Stageable {
    private Stage stage;
    @FXML
    private Label lblMessage;

    // Added @FXML fields that correspond to fx:id values in register-view.fxml
    @FXML
    private Label formLbl;

    @FXML
    private Label lblName;

    @FXML
    private TextField txtName;

    @FXML
    private Label lblEmail;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    @FXML
    public void initialize(){
        resetForm();
    }

    @FXML
    public void cancel(){
        stage.setScene(SceneFxmlApp.getScenes().get(SceneName.MAIN).getScene());
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void register(ActionEvent event) {
        String fullname = txtName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        boolean success = UserUtil.user_exist(email);
        if(!success){
            lblMessage.setText("Registration successful!");
            UserUtil.register(fullname, email, password);

        }else{
            lblMessage.setText("Registration failed. Please try again.");
        }
        resetForm();
        stage.setScene(SceneFxmlApp.getScenes().get(SceneName.MAIN).getScene());
    }

    public void resetForm(){
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        lblMessage.setText("");
    }
}
