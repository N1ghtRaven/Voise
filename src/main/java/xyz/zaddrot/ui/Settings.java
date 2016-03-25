package xyz.zaddrot.ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.yaml.snakeyaml.Yaml;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by night on 19.03.2016.
 */
public class Settings {

    @FXML
    private void initialize() {
        //TODO: Необходим метод предзагрузни панелей из конфига
        try {
            loadPane();
        }catch (Exception e){ e.printStackTrace();}
    }

    @FXML
    private TextField Symbol;
    @FXML
    public void onRelease(KeyEvent event) { Symbol.setText(event.getCode().getName()); }

    int id = 1;
    @FXML
    private VBox Vbox;
    @FXML
    private AnchorPane Scroll2;
    @FXML
    private ScrollPane ScrollPane;

    @FXML
    public void onPressAdd() {
        HBox node = new HBox();
        node.setAlignment(Pos.TOP_LEFT);
        node.setPadding(new Insets(10, 0, 0, 35));
        node.setSpacing(5);
        node.setId("HBox"+id);

        Label label = new Label("Команда "+id+": ");
        label.setPadding(new Insets(3, 0, 0, 0));
        node.getChildren().add(label);

        TextField newCommand = new TextField();

        newCommand.setId("newCommand_"+id);
        newCommand.setEditable(true);
        newCommand.setPrefHeight(23);
        newCommand.setPrefWidth(128);
        newCommand.setPromptText("Голосовая команда");

        TextField newFile = new TextField();
        newFile.setId("newFile_"+id);
        newFile.setEditable(false);
        newFile.setPrefHeight(22);
        newFile.setPrefWidth(128);
        newFile.setPromptText("Файл");

        Button newFileSelector = new Button("...");
        newFileSelector.setOnMouseClicked(((MouseEvent event2) -> {
            final FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(Main.setting_window);
            newFile.setPromptText(file.getAbsolutePath());
            newFile.setText(file.getName());
        }));


        newFileSelector.minHeight(16);
        newFileSelector.setPrefHeight(24);
        newFileSelector.setMnemonicParsing(false);

        Button newDelPane = new Button("-");
        newDelPane.setOnMouseClicked(((MouseEvent event) -> {
            int remove = 1;
            for(int i = 1; i < Vbox.getChildren().size(); i++){
                if(Vbox.getChildren().get(i).getId().equals(node.getId())) {
                    Vbox.getChildren().remove(i);
                    remove = i;
                }
            }

            for(int i = remove++; i < Vbox.getChildren().size(); i++){
                HBox node1 = (HBox) Vbox.getChildren().get(i);

                //Обновление имени лайбла (без ID)
                Label l = (Label) node1.getChildren().get(0);
                l.setText("Команда " + Integer.valueOf(Integer.valueOf(l.getText().replaceFirst("Команда ","").replaceFirst(":","").replaceAll(" ",""))-1) + ": ");

                TextField t1 = (TextField) node1.getChildren().get(1);
                t1.setId("newCommand_"+ Integer.valueOf(Integer.valueOf(t1.getId().replaceFirst("newCommand_",""))-1));

                //Обновление ID филда с текстом (для записи берём PromptText)
                TextField t2 = (TextField) node1.getChildren().get(2);
                t2.setId("newFile_"+ Integer.valueOf(Integer.valueOf(t2.getId().replaceFirst("newFile_",""))-1));

            }
            id--;
        }));
        newDelPane.minHeight(16);
        newDelPane.setPrefHeight(24);
        newDelPane.setMnemonicParsing(false);

        node.getChildren().add(newCommand);
        node.getChildren().add(newFile);
        node.getChildren().add(newFileSelector);
        node.getChildren().add(newDelPane);

        Vbox.getChildren().add(node);
        ++id;

        if(id > 6) {
            Scroll2.setPrefHeight(Scroll2.getPrefHeight()+30);
            ScrollPane.setVvalue(node.getPrefHeight()+30);
        }
    }

    @FXML
    public void onSave() {
        Map<String, Map> cfg = new HashMap<String, Map>();
        Yaml yaml = new Yaml();

        for(int i = 0; i < Vbox.getChildren().size(); ++i){
            HBox node1 = (HBox) Vbox.getChildren().get(i);
            Map<String, String> dataMap = new HashMap<String, String>();
            if(i == 0) {
                ComboBox c = (ComboBox) node1.getChildren().get(1);
                dataMap.put("mod", (String) c.getValue());
                TextField t1 = (TextField) node1.getChildren().get(2);
                dataMap.put("sym", t1.getText());
                cfg.put("HotKey",dataMap);
            }
            else{
                TextField c = (TextField) node1.getChildren().get(1);
                try {
                    dataMap.put("com", new String(c.getText().getBytes("UTF-8"), "Cp1251"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                TextField t2 = (TextField) node1.getChildren().get(2);
                dataMap.put("pth", t2.getPromptText());
                cfg.put("Command_"+i,dataMap);
            }
        }

        FileWriter writer = null;
        try{ writer = new FileWriter(Constance.CFG_COMMANDS); }catch (IOException e) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка при сохранении настроек");
            alert.setContentText("Закройте все программы использующие файл настроек.");

            alert.showAndWait();
            //TODO: UI_KIT ошибка
        }
        yaml.dump(cfg, writer);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Системное сообщение");
        alert.setHeaderText(null);
        alert.setContentText("Настройки сохранены успешно");

        alert.showAndWait();
        //TODO: UI_KIT Всё ОК
    }

    @FXML
    private ComboBox ComboBox;

    private void loadPane(){
        Map<String, Map> cfg = new HashMap<String, Map>();
        Yaml yaml = new Yaml();
        try{ cfg = (Map<String, Map>) yaml.load(new FileInputStream(Constance.CFG_COMMANDS)); }catch (FileNotFoundException e) {}

        Map<String, String> hotKey = cfg.get("HotKey");
        Symbol.setText(hotKey.get("sym"));
        ComboBox.setValue(hotKey.get("mod"));

        for(int i = 1;i < cfg.size();++i){

            Map<String, String> command = cfg.get("Command_"+i);
            String CommandText = command.get("com");
            String FilePath = command.get("pth");

            HBox node = new HBox();
            node.setAlignment(Pos.TOP_LEFT);
            node.setPadding(new Insets(10, 0, 0, 35));
            node.setSpacing(5);
            node.setId("HBox" + id);

            Label label = new Label("Команда "+id+": ");
            label.setPadding(new Insets(3, 0, 0, 0));
            node.getChildren().add(label);

            TextField newCommand = new TextField();
            newCommand.setId("newCommand_" + id);
            newCommand.setEditable(true);
            newCommand.setPrefHeight(23);
            newCommand.setPrefWidth(128);
            newCommand.setPromptText("Голосовая команда");
            newCommand.setText(CommandText);

            TextField newFile = new TextField();
            newFile.setId("newFile_" + id);
            newFile.setEditable(false);
            newFile.setPrefHeight(22);
            newFile.setPrefWidth(128);
            newFile.setPromptText(FilePath);
            newFile.setText(FilePath);

            Button newFileSelector = new Button("...");
            newFileSelector.setOnMouseClicked(((MouseEvent event2) -> {
                final FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(Main.setting_window);
                newFile.setPromptText(file.getAbsolutePath());
                newFile.setText(file.getName());
            }));

            newFileSelector.minHeight(16);
            newFileSelector.setPrefHeight(24);
            newFileSelector.setMnemonicParsing(false);

            Button newDelPane = new Button("-");
            newDelPane.setOnMouseClicked(((MouseEvent event) -> {
                int remove = 1;
                for (int i2 = 1; i2 < Vbox.getChildren().size(); i2++) {
                    if (Vbox.getChildren().get(i2).getId().equals(node.getId())) {
                        Vbox.getChildren().remove(i2);
                        remove = i2;
                    }
                }

                for (int i2 = remove++; i2 < Vbox.getChildren().size(); i2++) {
                    HBox node1 = (HBox) Vbox.getChildren().get(i2);

                    //Обновление имени лайбла (без ID)
                    Label l = (Label) node1.getChildren().get(0);
                    l.setText("Команда " + Integer.valueOf(Integer.valueOf(l.getText().replaceFirst("Команда ", "").replaceFirst(":", "").replaceAll(" ", "")) - 1) + ": ");

                    TextField t1 = (TextField) node1.getChildren().get(1);
                    t1.setId("newCommand_" + Integer.valueOf(Integer.valueOf(t1.getId().replaceFirst("newCommand_", "")) - 1));

                    //Обновление ID филда с текстом (для записи берём PromptText)
                    TextField t2 = (TextField) node1.getChildren().get(2);
                    t2.setId("newFile_" + Integer.valueOf(Integer.valueOf(t2.getId().replaceFirst("newFile_", "")) - 1));
                }
                id--;
            }));
            newDelPane.minHeight(16);
            newDelPane.setPrefHeight(24);
            newDelPane.setMnemonicParsing(false);

            node.getChildren().add(newCommand);
            node.getChildren().add(newFile);
            node.getChildren().add(newFileSelector);
            node.getChildren().add(newDelPane);

            Vbox.getChildren().add(node);
            ++id;

            if (id > 6) {
                Scroll2.setPrefHeight(Scroll2.getPrefHeight() + 30);
                ScrollPane.setVvalue(node.getPrefHeight() + 30);
            }
        }
    }
}
