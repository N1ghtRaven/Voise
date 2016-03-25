package xyz.zaddrot.ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import xyz.zaddrot.bind.Bind;

/**
 * Created by night on 19.03.2016.
 */
public class Main extends Application {
    private static Stage main_window;
    public static Stage setting_window;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        main_window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle(Constance.MAIN_TITLE);
        main_window.getIcons().add(new Image(Constance.MAIN_ICO));
        primaryStage.setScene(new Scene(root, primaryStage.getWidth()-5, primaryStage.getHeight()-50 ));
        //TODO: Метод загрузки положения окна на экране (Главный)
        Constance.CFG_PATH.mkdir();
        Map<String, Integer> cfg = null;
        Yaml yaml = new Yaml();
        try{ cfg = (Map<String, Integer>) yaml.load(new FileInputStream(Constance.CFG_WINDOW)); }catch (FileNotFoundException e) {}
        try{
            primaryStage.setX(cfg.get("main_x"));
            primaryStage.setY(cfg.get("main_y"));
        }catch (NullPointerException e){
            primaryStage.setX(100);
            primaryStage.setY(100);
        }
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            //TODO: Метод записи положения окна на экране в конфиг (Главный)
            Map<String, Integer> data = new HashMap<String, Integer>();
            data.put("main_x", (int) main_window.getX());
            data.put("main_y", (int) main_window.getY());

            try {
                data.put("setting_x", (int) main_window.getX());
                data.put("setting_y", (int) main_window.getY());
            }catch (NullPointerException ex){}
            FileWriter writer = null;
            try{ writer = new FileWriter(Constance.CFG_WINDOW); }catch (IOException e) {}
            yaml.dump(data, writer);
            System.exit(0);
        });
        primaryStage.show();
    }
    @FXML
    private void initialize() {
        if(Constance.CFG_COMMANDS.exists()) {
            //TODO: Индикатор записи звука
            button.setDisable(false);
            status.setText(Constance.STATUS_OFF);
        }
    }

    @FXML
    private Label status;
    @FXML
    private ToggleButton button;

    @FXML
    private void onBinderButton() throws InterruptedException {
        if(button.isSelected()){
            button.setTextFill(Paint.valueOf("#cc0000"));
            status.setText(Constance.STATUS_ON);
            Bind.start();
            //TODO: Вызов слушателя (HIGH)
        }
        else {
            button.setTextFill(Paint.valueOf("#00a610"));
            status.setText(Constance.STATUS_OFF);
            Bind.stop();
            //TODO: Остановка слушателя (HIGH)
        }
    }

    @FXML
    private void onSetting() throws IOException{
        main_window.close();
        Stage stage = new Stage();
        setting_window = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/settings.fxml"));
        setting_window.setTitle(Constance.SETTING_TITLE);
        setting_window.getIcons().add(new Image(Constance.SETTING_ICON));
        setting_window.setScene(new Scene(root, stage.getWidth()-5, stage.getHeight()-60 ));

        setting_window.setResizable(false);

        //TODO: Метод загрузки положения окна на экране (Настройки)
        Map <String, Integer> cfg = null;
        Yaml yaml = new Yaml();
        try{ cfg = (Map<String, Integer>) yaml.load(new FileInputStream(Constance.CFG_WINDOW)); }catch (FileNotFoundException e) {}
        try{
            stage.setX(cfg.get("setting_x"));
            stage.setY(cfg.get("setting_y"));
        }catch (NullPointerException e){
            stage.setX(100);
            stage.setY(100);
        }

        setting_window.show();
        setting_window.setOnCloseRequest((WindowEvent we) -> {
            main_window.show();
            //TODO: Метод записи положения окна на экране в конфиг (Настройки)
            Map<String, Integer> data = new HashMap<String, Integer>();
            data.put("main_x", (int) main_window.getX());
            data.put("main_y", (int) main_window.getY());

            data.put("setting_x", (int) setting_window.getX());
            data.put("setting_y", (int) setting_window.getY());

            FileWriter writer = null;
            try{ writer = new FileWriter(Constance.CFG_WINDOW); }catch (IOException e) {}
            yaml.dump(data, writer);

            if(Constance.CFG_COMMANDS.exists()) {
                button.setDisable(false);
                status.setText(Constance.STATUS_OFF);
            }
        });
    }
}
