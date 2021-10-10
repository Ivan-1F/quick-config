package me.ivan1f.quickconfig.example.setting;

import me.ivan1f.quickconfig.setting.Category;
import me.ivan1f.quickconfig.setting.ChangeHandler;
import me.ivan1f.quickconfig.setting.Setting;
import me.ivan1f.quickconfig.setting.WithHotkey;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

@Category
@SuppressWarnings("unused")
public class Tweaks {
    @Setting
    @WithHotkey(hotkey = "R + B")
    public static boolean invisibleCommandBlocks = false;
    @ChangeHandler(of = "invisibleCommandBlocks")
    public static final Consumer<Boolean> invisibleCommandBlocksChangeHandler = (value) -> {
        System.out.println("change");
//        MinecraftClient.getInstance().worldRenderer.reload();
    };
    @Setting public static String exampleStringConfig = "";
    @Setting public static boolean exampleSetting1 = false;
    @Setting public static boolean exampleSetting2 = false;
    @Setting public static boolean exampleSetting3 = false;
    @Setting public static boolean exampleSetting4 = false;
    @Setting public static boolean exampleSetting5 = false;
    @Setting public static boolean exampleSetting6 = false;
    @Setting public static boolean exampleSetting7 = false;
    @Setting public static boolean exampleSetting8 = false;
    @Setting public static boolean exampleSetting9 = false;
}
