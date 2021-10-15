package me.ivan1f.quickconfig.gui.widget;

import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.LiteralText;

public class TextInputWidget extends TextFieldWidget {
    public TextInputWidget(ParsedSetting<String> setting) {
        super(MinecraftClient.getInstance().textRenderer, 0, 0, 80, 20, new LiteralText(setting.value));
        this.setText(setting.value);
        this.setChangedListener(setting::set);
    }
}
