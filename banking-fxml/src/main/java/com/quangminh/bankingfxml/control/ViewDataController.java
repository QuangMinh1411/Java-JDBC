package com.quangminh.bankingfxml.control;

import com.quangminh.bankingfxml.model.Stageable;
import com.quangminh.bankingfxml.model.User;
import com.quangminh.bankingfxml.util.UserUtil;
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
    private TableView<User> tabelUser;
    @FXML
    public void initialize() {
        tabelUser = new TableView<>(UserUtil.getUsers());
        tabelUser.getColumns().addAll(
                UserUtil.getColumnByName(),
                UserUtil.getColumnEmail(),
                UserUtil.getColumnPassword()
        );
        tabelUser.setPrefWidth(280);
        userBox.getChildren().add(tabelUser);
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
