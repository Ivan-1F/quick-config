package me.ivan1f.quickconfig.translation;

public class TranslationUtils {
    /**
     * exampleMod.categories.Tweaks.someTweak -> example_mod.categories.tweaks.some_tweak
     * @param key exampleMod.categories.Tweaks.someTweak
     * @return example_mod.categories.tweaks.some_tweak
     */
    public static String jsonlizeTranslationKey(String key) {
        StringBuilder ret = new StringBuilder();
        for (String component : key.split("\\.")) {
            StringBuilder parsed = new StringBuilder();
            for (int i = 0; i < component.length(); i++) {
                char c = component.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    if (parsed.length() != 0) parsed.append("_");
                    parsed.append((char) (c - 'A' + 'a'));
                } else {
                    parsed.append(c);
                }
            }
            if (ret.length() != 0) ret.append(".");
            ret.append(parsed);
        }
        return ret.toString();
    }
}
