package me.ivan1f.quickconfig;

import java.util.List;

public interface QuickConfigExtension {
    List<Class<?>> getCategories();
    default String getOpenGuiHotkey() {
        return "J + C";
    }
}
