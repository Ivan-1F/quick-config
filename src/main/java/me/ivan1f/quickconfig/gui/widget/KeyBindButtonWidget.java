package me.ivan1f.quickconfig.gui.widget;

import me.ivan1f.quickconfig.gui.ExtensionScreen;
import me.ivan1f.quickconfig.keyboard.KeyCodes;
import me.ivan1f.quickconfig.keyboard.MultiKeyBind;
import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

public class KeyBindButtonWidget extends ButtonWidget {
    public boolean selected = false;
    public boolean firstKey = false;
    private final ParsedSetting<?> setting;
    private final MultiKeyBind hotkey;
    private final ExtensionScreen parent;

    public KeyBindButtonWidget(ParsedSetting<?> setting, ExtensionScreen parent) {
        super(0, 0, 120, 20, setting.hotkey.toString(), btn -> {
        });
        this.setting = setting;
        this.hotkey = setting.hotkey;
        this.parent = parent;
        this.updateDisplayString();
    }

    public void onKeyPressed(int keyCode) {
        if (this.selected) {
            if (keyCode == KeyCodes.KEY_ESCAPE) {
                if (this.firstKey) {
                    this.hotkey.clearKeys();
                }
                if (this.parent != null) {
                    this.parent.setActiveKeyBindButton(null);
                }
            } else {
                this.addKey(keyCode);
            }
            this.updateDisplayString();
        }
    }

    private void addKey(int keyCode) {
        if (this.firstKey) {
            this.hotkey.clearKeys();
            this.firstKey = false;
        }

        this.hotkey.addKey(keyCode);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (this.selected) {
            this.addKey(button - 100);
            this.updateDisplayString();
        } else if (button == 0) {
            this.selected = true;
            if (this.parent != null) {
                this.parent.setActiveKeyBindButton(this);
            }
        }

        return true;
    }

    public void onSelected() {
        this.selected = true;
        this.firstKey = true;
        this.updateDisplayString();
    }

    public void onClearSelection() {
        this.selected = false;
        setting.hotkey = this.hotkey;
        this.updateDisplayString();
    }

    public void updateDisplayString() {
        this.setMessage(this.hotkey.toString());
        String keyBindStr = this.hotkey.toString();

        if (this.hotkey.getKeyCodes().size() == 0 || StringUtils.isBlank(keyBindStr)) {
            keyBindStr = "NONE";
        }

        if (this.selected) {
            this.setMessage("> " + Formatting.YELLOW + keyBindStr + Formatting.RESET + " <");
        } else {
            this.setMessage(keyBindStr);
        }
    }
}
