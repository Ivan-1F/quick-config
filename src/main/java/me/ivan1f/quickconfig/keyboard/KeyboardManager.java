package me.ivan1f.quickconfig.keyboard;

import me.ivan1f.quickconfig.extension.ExtensionManager;
import me.ivan1f.quickconfig.extension.ParsedExtension;
import me.ivan1f.quickconfig.gui.ExtensionScreen;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class KeyboardManager {
    public static final ArrayList<Integer> PRESSED_KEYS = new ArrayList<>();
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void onMultikeyUpdate(String keys) {
        if (client.currentScreen == null) {
            for (ParsedExtension extension : ExtensionManager.extensions) {
                if (extension.openGuiKey.isPressed()) {
                    System.out.println("Open gui of " + extension);
                    client.openScreen(new ExtensionScreen(extension));
                }
                extension.onMultikeyUpdate(new MultiKeyBind(keys));
            }
        }
    }

    public static void onKey(int key, int scancode, int action, int j) {
        Integer keycode = key;  // Cast to object
        if (action != 0) {
            // Down
            if (!PRESSED_KEYS.contains(keycode)) {
                PRESSED_KEYS.add(keycode);
            }
        } else {
            // Up
            PRESSED_KEYS.remove(keycode);
        }
        onMultikeyUpdate(getActiveKeysString());
    }

    public static String getActiveKeysString() {
        return KeyCodes.keycodesToString(PRESSED_KEYS);
    }

}
