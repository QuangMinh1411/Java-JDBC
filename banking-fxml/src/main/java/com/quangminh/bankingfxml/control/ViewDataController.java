package com.quangminh.bankingfxml.control;

import com.quangminh.bankingfxml.SceneFxmlApp;
import com.quangminh.bankingfxml.model.Accounts;
import com.quangminh.bankingfxml.model.SceneName;
import com.quangminh.bankingfxml.model.Stageable;
import com.quangminh.bankingfxml.model.User;
import com.quangminh.bankingfxml.util.AccountUtil;
import com.quangminh.bankingfxml.util.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ViewDataController implements Stageable {
    private Stage stage;
    @FXML
    private HBox viewBox;
    @FXML
    private HBox userBox;
    @FXML
    private HBox accountBox;
    private TableView<User> tableUser;
    private TableView<Accounts> tableAccounts;
    @FXML
    public void initialize() {
        tableUser = new TableView<>(UserUtil.getUsers());
        tableUser.getColumns().addAll(
                UserUtil.getColumnByName(),
                UserUtil.getColumnEmail(),
                UserUtil.getColumnPassword()
        );
        tableUser.setPrefWidth(280);

        tableAccounts = new TableView<>(AccountUtil.getAccounts());
        tableAccounts.getColumns().addAll(
                AccountUtil.getColumnAccountNumber(),
                AccountUtil.getColumnFullName(),
                AccountUtil.getColumnEmail(),
                AccountUtil.getColumnBalance(),
                AccountUtil.getColumnSecurityPin()
        );
        tableAccounts.setPrefWidth(800);
        accountBox.getChildren().add(tableAccounts);
        userBox.getChildren().add(tableUser);

    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void back(ActionEvent event) {
        stage.setScene(SceneFxmlApp.getScenes().get(SceneName.MAIN).getScene());
    }
}
