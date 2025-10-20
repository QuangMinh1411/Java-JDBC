package com.quangminh.bankingfxml.util;

import com.quangminh.bankingfxml.model.SceneName;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxmlInfo {
    private String resourceName;
    private SceneName sceneName;
    private Stage stage;
    private Scene scene;

    public FxmlInfo(String resourceName, SceneName sceneName, Stage stage) {
        this.resourceName = resourceName;
        this.sceneName = sceneName;
        this.stage = stage;
    }
    public String getResourceName() {
        return resourceName;
    }
    public SceneName getSceneName() {
        return sceneName;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public Scene getScene() {
        if(scene == null) {
            scene = new FxmlLoad().load(this);
        }
        return scene;
    }
    public boolean hasScene() {
        return scene != null;
    }

    public Stage getStage() {
        return stage;
    }
}
