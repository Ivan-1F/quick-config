package me.ivan1f.quickconfig.example.setting;

import me.ivan1f.quickconfig.setting.Category;
import me.ivan1f.quickconfig.setting.Setting;

@Category
public class Disables {
    @Setting(comment = true) public static boolean disableEndermanNoise = false;
}
