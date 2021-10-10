package me.ivan1f.quickconfig.example.setting;

import me.ivan1f.quickconfig.setting.Category;
import me.ivan1f.quickconfig.setting.Setting;
import me.ivan1f.quickconfig.setting.WithHotkey;

@Category
public class Tweaks {
    @Setting
    @WithHotkey(hotkey = "R + B")
    public static boolean invisibleCommandBlocks = false;
}
