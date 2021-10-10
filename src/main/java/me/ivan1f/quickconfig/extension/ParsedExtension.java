package me.ivan1f.quickconfig.extension;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.ivan1f.quickconfig.QuickConfigExtension;
import me.ivan1f.quickconfig.keyboard.MultiKeyBind;
import me.ivan1f.quickconfig.setting.ParsedCategory;
import me.ivan1f.quickconfig.setting.ParsedSetting;
import me.ivan1f.quickconfig.translation.INamedObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParsedExtension implements INamedObject {
    public List<ParsedCategory> categories = new ArrayList<>();
    public String name;
    public String displayName;
    public MultiKeyBind openGuiKey;
    public File configFile;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public ParsedExtension(QuickConfigExtension extension) {
        this.name = extension.getClass().getSimpleName();
        this.displayName = extension.getDisplayName().equals("") ? this.name : extension.getDisplayName();
        this.openGuiKey = new MultiKeyBind(extension.getOpenGuiHotkey());
        this.configFile = new File("./config", this.name + ".json");
        for (Class<?> category : extension.getCategories()) {
            try {
                this.categories.add(new ParsedCategory(category, this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.loadConfig();
    }

    @Override
    public String getTranslationKey() {
        return this.name + ".name";
    }

    public void loadConfig() {
        if ((this.configFile.exists() && this.configFile.isFile() && this.configFile.canRead())) {
            JsonParser parser = new JsonParser();
            try {
                JsonElement element = parser.parse(new FileReader(this.configFile));
                JsonObject root = element.getAsJsonObject();
                for (ParsedCategory category : this.categories) {
                    JsonObject categoryObj = root.getAsJsonObject(category.name);
                    for (ParsedSetting<?> setting : category.settings) {
                        JsonObject settingObj = categoryObj.getAsJsonObject(setting.name);
                        if (setting.withHotkey) {
                            setting.hotkey = new MultiKeyBind(settingObj.get("hotkey").getAsString());
                        }
                        if (setting.type == boolean.class) {
                            setting.set(settingObj.get("value").getAsBoolean());
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            this.saveConfig();
        }
    }

    public void saveConfig() {
        JsonObject root = new JsonObject();
        for (ParsedCategory category : this.categories) {
            JsonObject categoryObj = new JsonObject();
            for (ParsedSetting<?> setting : category.settings) {
                JsonObject settingObj = new JsonObject();
                settingObj.addProperty("value", String.valueOf(setting.value));
                if (setting.withHotkey) {
                    settingObj.addProperty("hotkey", setting.hotkey.toString());
                }
                categoryObj.add(setting.name, settingObj);
            }
            root.add(category.name, categoryObj);
        }
        try {
            if ((this.configFile.exists() || this.configFile.createNewFile()) && this.configFile.isFile() && this.configFile.canWrite()) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.configFile));
                bufferedWriter.write(root.toString());
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
