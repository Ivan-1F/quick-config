package me.ivan1f.quickconfig.keyboard;

import me.ivan1f.quickconfig.extension.ExtensionManager;
import me.ivan1f.quickconfig.extension.ParsedExtension;
import me.ivan1f.quickconfig.gui.ExtensionScreen;
import me.ivan1f.quickconfig.setting.ParsedCategory;
import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class KeyboardManager {
    public static final ArrayList<Integer> PRESSED_KEYS = new ArrayList<>();
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void onMultikeyUpdate(String keys) {
        if (client.currentScreen == null) {
            for (ParsedExtension extension : ExtensionManager.extensions) {
                if (extension.openGuiKey.equals(keys)) {
                    System.out.println("Open gui of " + extension);
                    client.openScreen(new ExtensionScreen(extension));
                }
                for (ParsedCategory category : extension.categories) {
                    for (ParsedSetting<?> setting : category.settings) {
                        if (setting.withHotkey && setting.hotkey.equals(keys) && setting.type == boolean.class) {
                            setting.set(!((boolean) setting.value));
                            if (client.player != null) {
                                Text status = new LiteralText(
                                        (boolean) setting.value ? Formatting.GREEN + "ON" : Formatting.RED + "OFF"
                                );
                                client.player.addChatMessage(
                                        new TranslatableText(setting.displayName).append(" ").append(status), true
                                );
                            }
                            System.out.println(setting.value);
                        }
                    }
                }
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
        onMultikeyUpdate(getStringValue());
    }

    public static String getDisplayString() {
        return getStringValue().replaceAll(",", " + ");
    }

    public static String getStringValue() {
        StringBuilder sb = new StringBuilder(32);

        for (int i = 0; i < PRESSED_KEYS.size(); ++i) {
            if (i > 0) {
                sb.append(",");
            }

            int keyCode = PRESSED_KEYS.get(i);
            String name = getStorageStringForKeyCode(keyCode);

            if (name != null) {
                sb.append(name);
            }
        }

        return sb.toString();
    }

    @Nullable
    public static String getStorageStringForKeyCode(int keyCode) {
        return KeyCodes.getNameForKey(keyCode);
    }
}
