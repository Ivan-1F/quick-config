package me.ivan1f.quickconfig.extension;

import me.ivan1f.quickconfig.QuickConfigExtension;
import me.ivan1f.quickconfig.keyboard.MultiKeyBind;
import me.ivan1f.quickconfig.setting.ParsedCategory;
import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ParsedExtension {
    public List<ParsedCategory> categories = new ArrayList<>();
    public String displayName;
    public MultiKeyBind openGuiKey;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public ParsedExtension(QuickConfigExtension extension) {
        for (Class<?> category : extension.getCategories()) {
            try {
                this.categories.add(new ParsedCategory(category));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.displayName = extension.getDisplayName().equals("") ? extension.getClass().getSimpleName() : extension.getDisplayName();
        this.openGuiKey = new MultiKeyBind(extension.getOpenGuiHotkey());
    }

    public void onMultikeyUpdate(MultiKeyBind key) {
        for (ParsedCategory category : this.categories) {
            for (ParsedSetting<?> setting : category.settings) {
                if (setting.withHotkey && setting.hotkey.isPressed() && setting.type == boolean.class) {
                    setting.set(!((boolean) setting.value));
                    if (client.player != null) {
                        Text status = new LiteralText(
                                (boolean) setting.value ? Formatting.GREEN + "ON" : Formatting.RED + "OFF"
                        );
                        client.player.addChatMessage(
                                new TranslatableText(setting.displayName).append(" ").append(status), true
                        );
                    }
                }
            }
        }
    }
}
