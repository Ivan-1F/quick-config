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
        MinecraftClient.getInstance().worldRenderer.reload();
    };
}
