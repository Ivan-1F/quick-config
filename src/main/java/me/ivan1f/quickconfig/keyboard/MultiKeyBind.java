package me.ivan1f.quickconfig.keyboard;

import java.util.ArrayList;
import java.util.List;

public class MultiKeyBind {
    private List<Integer> keyCodes = new ArrayList<>();
    private String keys;

    public MultiKeyBind(String keys) {
        this.keys = keys;
        this.setValueFromString();
    }

    public void clearKeys() {
        this.keyCodes.clear();
        this.keys = "";
    }

    public void addKey(int keyCode) {
        if (!this.keyCodes.contains(keyCode)) {
            this.keyCodes.add(keyCode);
            this.setStringFromValue();
        }
    }

    public void setValueFromString() {
        this.keyCodes = KeyCodes.stringToKeycodes(this.keys);
    }

    public void setStringFromValue() {
        this.keys = KeyCodes.keycodesToString(this.keyCodes);
    }

    @Override
    public String toString() {
        return this.keys;
    }

    public List<Integer> getKeyCodes() {
        return keyCodes;
    }

    public boolean isPressed() {
        return this.keys.equals(KeyboardManager.getActiveKeysString());
    }
}
