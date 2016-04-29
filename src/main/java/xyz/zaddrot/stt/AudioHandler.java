package xyz.zaddrot.stt;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.yaml.snakeyaml.Yaml;
import xyz.zaddrot.ui.Constance;
import xyz.zaddrot.ui.Main;

import javax.net.ssl.HttpsURLConnection;
import javax.sound.sampled.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by night on 20.03.2016.
 */
public class AudioHandler {
    public static void main(){
        Map<String, Map> cfg = new HashMap<>();
        Yaml yaml = new Yaml();
        try{ cfg = (Map<String, Map>) yaml.load(new FileInputStream(Constance.CFG_COMMANDS)); }catch (FileNotFoundException e) {}
        String sourceCommand = detectCommand(recorder()).toString().toLowerCase();
        System.out.println(sourceCommand);
        for(int i = 1;i < cfg.size();++i){
            Map<String, String> command = cfg.get("Command_"+i);
            if(sourceCommand.equalsIgnoreCase(command.get("com").toString())) {
                try { Runtime.getRuntime().exec("cmd /c "+command.get("pth")); } catch (IOException e) {e.printStackTrace();
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText(null);
                            alert.setContentText("Ошибка при выполнении команды.");
                            alert.showAndWait();
                        }});
                }
            }
        }
    }

    private static File recorder(){
        try{
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,16,2,4,44100,false);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if(!AudioSystem.isLineSupported(info)) {  Alert alert = new Alert(Alert.AlertType.ERROR);
                Platform.runLater(() -> {
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка при записи голоса");
                alert.setContentText("Запись не поддерживается устройством.");
                alert.showAndWait();
            });
            }

            final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
            targetLine.open();

            new Thread(() -> { playSound(); }).start();
            System.out.println("Идёт запись...");
            targetLine.start();
            Constance.AUDIO_PATH.mkdir();
            File audioFile = Constance.AUDIO_FILE;
            Thread thread = new Thread() {
                @Override
                public void run(){
                    AudioInputStream audioStream = new AudioInputStream(targetLine);
                    try { AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile); } catch (IOException e) {  Alert alert = new Alert(Alert.AlertType.ERROR);
                        Platform.runLater(() -> {
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Ошибка при сохранении голоса");
                            alert.setContentText("Убедитесь что звуковой файл rec.mp3 не используется другими программами.");
                            alert.showAndWait();
                        });
                    }
                    System.out.println("Запись завершена");
                    new Thread(() -> { playSound(); }).start();
                }
            };
            thread.start();
            Thread.sleep(5000);
            targetLine.stop();
            targetLine.close();

            System.out.println("Конец записи");

            new Thread(() -> { playSound(); }).start();

            return audioFile;
        }catch(InterruptedException | LineUnavailableException e) { e.printStackTrace(); }
        return null;
    }

    private static String detectCommand(File file){
        try {
            URL url = new URL(Constance.YA_URI);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "audio/x-wav");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; ru; rv:1.8.1.12) Gecko/20080201 Firefox");
            OutputStream out = connection.getOutputStream();
            InputStream comFile = new FileInputStream(file);
            byte[] fileBArray = new byte[4096];
            for (int length; (length = comFile.read(fileBArray)) > 0; ) {
                out.write(fileBArray, 0, length);
            }
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String decodedString;
            StringBuffer response = new StringBuffer();
            while ((decodedString = in.readLine()) != null) { response.append(decodedString); }
            in.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(response.toString()));
            Document doc = builder.parse(is);

            return new String(doc.getDocumentElement().getElementsByTagName("variant").item(0).getChildNodes().item(0).getNodeValue().getBytes("Cp1251"), "UTF-8");
        }catch (Exception ex){ ex.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка при обработке голоса");
                alert.setContentText("Проверьте правильность записи голоса, наличие подключения к Интернету.");
                alert.showAndWait();
            });
        }
        return null;
    }

    public static void playSound() {
        InputStream fis = Main.class.getResourceAsStream(Constance.SOUND_RECORD_ON);
        BufferedInputStream bis = new BufferedInputStream(fis);
        try{
            Player player = new Player(bis);
            player.play();
        }catch(JavaLayerException ex){
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка запуска звукового файла");
                    alert.setContentText("Проверьте настройки вашей аудиокарты.");
                    alert.showAndWait();
                }});
        }
    }
}
