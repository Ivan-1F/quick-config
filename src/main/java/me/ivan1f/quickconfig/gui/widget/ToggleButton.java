package me.ivan1f.quickconfig.gui.widget;

import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Formatting;

public class ToggleButton extends ButtonWidget {
    public ToggleButton(ParsedSetting<?> setting) {
        super(0, 0, 40, 20, (boolean) setting.value ? Formatting.GREEN + "ON" : Formatting.RED + "OFF", button -> {
            setting.set(!((boolean) setting.value));
            button.setMessage((boolean) setting.value ? Formatting.GREEN + "ON" : Formatting.RED + "OFF");
        });
    }
}