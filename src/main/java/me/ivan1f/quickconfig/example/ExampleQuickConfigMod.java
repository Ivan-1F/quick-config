package me.ivan1f.quickconfig.example;

import com.google.common.collect.ImmutableList;
import me.ivan1f.quickconfig.QuickConfigExtension;
import me.ivan1f.quickconfig.example.setting.Disables;
import me.ivan1f.quickconfig.example.setting.Tweaks;
import me.ivan1f.quickconfig.extension.ExtensionManager;
import net.fabricmc.api.ModInitializer;

import java.util.List;

public class ExampleQuickConfigMod implements ModInitializer, QuickConfigExtension {
    @Override
    public void onInitialize() {
        ExtensionManager.manageExtension(this);
    }

    @Override
    public List<Class<?>> getCategories() {
        return ImmutableList.of(Tweaks.class, Disables.class);
    }

    @Override
    public String getOpenGuiHotkey() {
        return "R,C";
    }
}
