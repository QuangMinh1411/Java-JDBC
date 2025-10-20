package com.quangminh.bankingfxml.control;

import com.quangminh.bankingfxml.SceneFxmlApp;
import com.quangminh.bankingfxml.model.SceneName;
import com.quangminh.bankingfxml.model.Stageable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MainController implements Stageable {
    private Stage stage;
    @FXML
    private void register(){
        stage.setScene(SceneFxmlApp.getScenes().get(SceneName.REGISTER).getScene());
    }
    @FXML
    private void login(){
        stage.setScene(SceneFxmlApp.getScenes().get(SceneName.LOGIN).getScene());
    }

    @FXML
    private void exit(){
        stage.close();
    }
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void show(ActionEvent event) {
        stage.setScene(SceneFxmlApp.getScenes().get(SceneName.VIEW_DATA).getScene());
    }
}
