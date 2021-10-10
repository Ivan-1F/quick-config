package me.ivan1f.quickconfig.extension;

import me.ivan1f.quickconfig.QuickConfigExtension;

import java.util.ArrayList;
import java.util.List;

public class ExtensionManager {
    public static List<ParsedExtension> extensions = new ArrayList<>();
    public static void manageExtension(QuickConfigExtension extension) {
        extensions.add(new ParsedExtension(extension));
    }
}
