package com.quangminh.bankingfxml.control;

import com.quangminh.bankingfxml.SceneFxmlApp;
import com.quangminh.bankingfxml.model.Accounts;
import com.quangminh.bankingfxml.model.SceneName;
import com.quangminh.bankingfxml.model.Stageable;
import com.quangminh.bankingfxml.util.AccountUtil;
import com.quangminh.bankingfxml.util.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController implements Stageable {
    private Stage stage;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button logBtn;
    public static String success_email;
    public static Long accountNumber;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    public void initialize(){
        accountNumber = 0L;
        success_email=null;
    }

    public void logIn(ActionEvent event) {
        Window owner = logBtn.getScene().getWindow();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String login_email = UserUtil.login(email, password);
        if(login_email != null){
            success_email = login_email;
            System.out.println(success_email);
            if(!AccountUtil.account_exist(login_email)){
                stage.setScene(SceneFxmlApp.getScenes().get(SceneName.NEW_ACCOUNT).getScene());
            }else{
                accountNumber = AccountUtil.getAccountNumberByEmail(success_email);
                System.out.println(accountNumber);
                stage.setScene(SceneFxmlApp.getScenes().get(SceneName.TRANSACTION).getScene());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email or password. Please try again.");
            alert.initOwner(owner);
            alert.showAndWait();
            return;
        }
        resetFields();
    }

    public void resetFields() {
        txtEmail.clear();
        txtPassword.clear();
    }
}
