package xyz.zaddrot.ui;

import java.io.File;

/**
 * Created by night on 19.03.2016.
 */
public class Constance {
    final public static String MAIN_TITLE = "VoiseListener - 1.0.1";
    final public static String MAIN_ICO = "/images/mic.png";

    //TODO: Исправить UI
    final public static String STATUS_ON = "Комбинация перехватывается";
    final public static String STATUS_OFF = "Комбинация не перехватывается";

    final public static String SETTING_ICON = "/images/wrench.png";
    final public static String SETTING_TITLE = "Настройки";

    final public static File AUDIO_PATH = new File("cache");
    final public static File AUDIO_FILE = new File("cache/rec.wav");

    final public static File CFG_PATH = new File("configs");
    final public static File CFG_WINDOW = new File("configs/window.yaml");
    final public static File CFG_COMMANDS = new File("configs/commands.yaml");
}
