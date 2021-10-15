package me.ivan1f.quickconfig.gui.widget;

import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class ToggleButton extends ButtonWidget {
    public ToggleButton(ParsedSetting<Boolean> setting) {
        super(0, 0, 40, 20, new LiteralText(setting.value ? Formatting.GREEN + "ON" : Formatting.RED + "OFF"), button -> {
            setting.set(!setting.value);
            button.setMessage(new LiteralText(setting.value ? Formatting.GREEN + "ON" : Formatting.RED + "OFF"));
        });
    }
}