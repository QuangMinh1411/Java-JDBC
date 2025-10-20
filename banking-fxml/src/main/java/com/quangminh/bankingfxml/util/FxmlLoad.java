package com.quangminh.bankingfxml.util;

import com.quangminh.bankingfxml.SceneFxmlApp;
import com.quangminh.bankingfxml.model.Stageable;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class FxmlLoad {
    public Scene load(FxmlInfo fxmlInfo)  {
        if(fxmlInfo.hasScene()){
            return fxmlInfo.getScene();
        }
        URL url = getClass().getResource(fxmlInfo.getResourceName());
        if(url == null){
            Platform.exit();
            return null;
        }
        FXMLLoader loader = new FXMLLoader(url);
        Scene scene;
        try{
            scene = new Scene(loader.load());
        }catch(IOException e){
            e.printStackTrace();
            Platform.exit();
            return null;
        }
        fxmlInfo.setScene(scene);
        SceneFxmlApp.updateScenes(fxmlInfo.getSceneName(),fxmlInfo);
        Stageable controller = loader.getController();
        if(controller!=null){
            controller.setStage(fxmlInfo.getStage());
        }
        return scene;
    }

}
