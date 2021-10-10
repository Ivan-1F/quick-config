package me.ivan1f.quickconfig.extension;

import me.ivan1f.quickconfig.QuickConfigExtension;
import me.ivan1f.quickconfig.setting.ParsedCategory;

import java.util.ArrayList;
import java.util.List;

public class ParsedExtension {
    public List<ParsedCategory> categories = new ArrayList<>();
    public String displayName;
    public String openGuiKey;
    public ParsedExtension(QuickConfigExtension extension) {
        for (Class<?> category : extension.getCategories()) {
            try {
                this.categories.add(new ParsedCategory(category));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.displayName = extension.getDisplayName().equals("") ? extension.getClass().getName() : extension.getDisplayName();
        this.openGuiKey = extension.getOpenGuiHotkey();
    }
}
