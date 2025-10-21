package com.quangminh.bankingfxml;

import com.quangminh.bankingfxml.model.SceneName;
import com.quangminh.bankingfxml.util.FxmlInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneFxmlApp extends Application {
    private static final String MAIN_FXML = "/com/quangminh/bankingfxml/main-view.fxml";
    private static final String REGISTER_FXML = "/com/quangminh/bankingfxml/register-view.fxml";
    private static final String LOGIN_FXML = "/com/quangminh/bankingfxml/login-view.fxml";
    private static final String VIEW_DATA_FXML = "/com/quangminh/bankingfxml/data-view.fxml";
    private static final String NEW_ACCOUNT_FXML = "/com/quangminh/bankingfxml/new-account.fxml";
    private static final String TRANSACTION_FXML = "/com/quangminh/bankingfxml/transaction-view.fxml";
    private static Map<SceneName,FxmlInfo> scenes = new HashMap<>();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        scenes.put(SceneName.MAIN,new FxmlInfo(MAIN_FXML,SceneName.MAIN,stage));
        scenes.put(SceneName.REGISTER,new FxmlInfo(REGISTER_FXML,SceneName.REGISTER,stage));
        scenes.put(SceneName.LOGIN,new FxmlInfo(LOGIN_FXML,SceneName.LOGIN,stage));
        scenes.put(SceneName.VIEW_DATA,new FxmlInfo(VIEW_DATA_FXML,SceneName.VIEW_DATA,stage));
        scenes.put(SceneName.NEW_ACCOUNT,new FxmlInfo(NEW_ACCOUNT_FXML,SceneName.NEW_ACCOUNT,stage));
        scenes.put(SceneName.TRANSACTION,new FxmlInfo(TRANSACTION_FXML,SceneName.TRANSACTION,stage));
        stage.setTitle("Banking Application");
        stage.setScene(scenes.get(SceneName.MAIN).getScene());
        stage.show();
    }
    public static Map<SceneName,FxmlInfo> getScenes() {
        return  scenes;
    }
    public static void updateScenes(SceneName name,FxmlInfo info) {
        scenes.put(name,info);
    }
}
