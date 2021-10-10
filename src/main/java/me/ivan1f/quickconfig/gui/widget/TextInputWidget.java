package me.ivan1f.quickconfig.gui.widget;

import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class TextInputWidget extends TextFieldWidget {
    public TextInputWidget(ParsedSetting<?> setting) {
        super(MinecraftClient.getInstance().textRenderer, 0, 0, 80, 20, (String) setting.value);
        this.setText((String) setting.value);
        this.setChangedListener(setting::set);
    }
}
