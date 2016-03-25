package xyz.zaddrot.bind;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import org.yaml.snakeyaml.Yaml;
import xyz.zaddrot.ui.Constance;
import xyz.zaddrot.stt.AudioHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by night on 20.03.2016.
 */
public class Bind {
    private static HotkeyListener hotkeyListener = new HotkeyListener() {
        public void onHotKey (int i) {
            if(i == 0) {
                //TODO: Переход в метод парсинга голоса
                System.out.println("Main");
                AudioHandler.main();
            }
        }
    };

    public static void start() {
        if (System.getProperty("os.arch").equals("amd64")) {
            JIntellitype.setLibraryLocation("libs\\JIntellitype64.dll");
        } else {
            JIntellitype.setLibraryLocation("libs\\JIntellitype.dll");
        }

        Map<String, Map> cfg = new HashMap<>();
        Yaml yaml = new Yaml();
        try{ cfg = (Map<String, Map>) yaml.load(new FileInputStream(Constance.CFG_COMMANDS)); }catch (FileNotFoundException e) {}

        Map<String, String> hotKey = cfg.get("HotKey");
        String SymbolText = hotKey.get("sym");
        int sym = 0;

        if(SymbolText.toCharArray().length > 1) {
            switch(SymbolText){
                case "F1":
                    sym = java.awt.event.KeyEvent.VK_F1;
                    break;
                case "F2":
                    sym = java.awt.event.KeyEvent.VK_F2;
                    break;
                case "F3":
                    sym = java.awt.event.KeyEvent.VK_F3;
                    break;
                case "F4":
                    sym = java.awt.event.KeyEvent.VK_F4;
                    break;
                case "F5":
                    sym = java.awt.event.KeyEvent.VK_F5;
                    break;
                case "F6":
                    sym = java.awt.event.KeyEvent.VK_F6;
                    break;
                case "F7":
                    sym = java.awt.event.KeyEvent.VK_F7;
                    break;
                case "F8":
                    sym = java.awt.event.KeyEvent.VK_F8;
                    break;
                case "F9":
                    sym = java.awt.event.KeyEvent.VK_F9;
                    break;
                case "F10":
                    sym = java.awt.event.KeyEvent.VK_F10;
                    break;
                case "F11":
                    sym = java.awt.event.KeyEvent.VK_F11;
                    break;
                case "F12":
                    sym = java.awt.event.KeyEvent.VK_F12;
                    break;
                case "Esc":
                    sym = java.awt.event.KeyEvent.VK_ESCAPE;
                    break;
                case "Space":
                    sym = java.awt.event.KeyEvent.VK_SPACE;
                    break;
                case "Back Quote":
                    sym = java.awt.event.KeyEvent.VK_BACK_QUOTE;
                    break;
                case "Minus":
                    sym = java.awt.event.KeyEvent.VK_MINUS;
                    break;
                case "Equals":
                    sym = java.awt.event.KeyEvent.VK_EQUALS;
                    break;
                case "Back Slash":
                    sym = java.awt.event.KeyEvent.VK_BACK_SLASH;
                    break;
                case "Backspace":
                    sym = java.awt.event.KeyEvent.VK_BACK_SPACE;
                    break;
                case "Comma":
                    sym = java.awt.event.KeyEvent.VK_COMMA;
                    break;
                case "Period":
                    sym = java.awt.event.KeyEvent.VK_PERIOD;
                    break;
                case "Slash":
                    sym = java.awt.event.KeyEvent.VK_SLASH;
                    break;
                case "Semicolon":
                    sym = java.awt.event.KeyEvent.VK_SEMICOLON;
                    break;
                case "Quote":
                    sym = java.awt.event.KeyEvent.VK_QUOTE;
                    break;
                case "Open Bracket":
                    sym = java.awt.event.KeyEvent.VK_OPEN_BRACKET;
                    break;
                case "Close Bracket":
                    sym = java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
                    break;
                case "Home":
                    sym = java.awt.event.KeyEvent.VK_HOME;
                    break;
                case "End":
                    sym = java.awt.event.KeyEvent.VK_END;
                    break;
                case "Insert":
                    sym = java.awt.event.KeyEvent.VK_INSERT;
                    break;
                case "Page Up":
                    sym = java.awt.event.KeyEvent.VK_PAGE_UP;
                    break;
                case "Page Down":
                    sym = java.awt.event.KeyEvent.VK_PAGE_DOWN;
                    break;
                case "Delete":
                    sym = java.awt.event.KeyEvent.VK_DELETE;
                    break;
                case "Divide":
                    sym = java.awt.event.KeyEvent.VK_DIVIDE;
                    break;
                case "Multiply":
                    sym = java.awt.event.KeyEvent.VK_MULTIPLY;
                    break;
                case "Subtract":
                    sym = java.awt.event.KeyEvent.VK_SUBTRACT;
                    break;
                case "Add":
                    sym = java.awt.event.KeyEvent.VK_ADD;
                    break;
                case "Decimal":
                    sym = java.awt.event.KeyEvent.VK_DECIMAL;
                    break;
                case "Pause":
                    sym = java.awt.event.KeyEvent.VK_PAUSE;
                    break;
                case "Numpad 0":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD0;
                    break;
                case "Numpad 1":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD1;
                    break;
                case "Numpad 2":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD2;
                    break;
                case "Numpad 3":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD3;
                    break;
                case "Numpad 4":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD4;
                    break;
                case "Numpad 5":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD5;
                    break;
                case "Numpad 6":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD6;
                    break;
                case "Numpad 7":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD7;
                    break;
                case "Numpad 8":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD8;
                    break;
                case "Numpad 9":
                    sym = java.awt.event.KeyEvent.VK_NUMPAD9;
                    break;
                case "Up":
                    sym = java.awt.event.KeyEvent.VK_UP;
                    break;
                case "Down":
                    sym = java.awt.event.KeyEvent.VK_DOWN;
                    break;
                case "Left":
                    sym = java.awt.event.KeyEvent.VK_LEFT;
                    break;
                case "Right":
                    sym = java.awt.event.KeyEvent.VK_RIGHT;
                    break;
            }
        }
        else { sym = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(SymbolText.charAt(0)); }

        String ModValue = hotKey.get("mod");
        int mod = 0;
        switch(ModValue){
            case "Без модификатора":
                mod = 0;
                break;
            case "Alt":
                mod = JIntellitype.MOD_ALT;
                break;
            case "Shift":
                mod = JIntellitype.MOD_SHIFT;
                break;
            case "Win":
                mod = JIntellitype.MOD_WIN;
                break;
            case "Ctrl":
                mod = JIntellitype.MOD_CONTROL;
                break;
        }
        JIntellitype.getInstance().registerHotKey(0, mod, sym);
        JIntellitype.getInstance().addHotKeyListener(hotkeyListener);

    }

    public static void stop(){
        JIntellitype.getInstance().unregisterHotKey(0);
        JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
    }

}
