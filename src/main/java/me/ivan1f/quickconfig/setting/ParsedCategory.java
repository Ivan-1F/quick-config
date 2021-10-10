package me.ivan1f.quickconfig.setting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ParsedCategory {
    public List<ParsedSetting<?>> settings = new ArrayList<>();
    public String displayName;

    public ParsedCategory(Class<?> cls) throws Exception {
        for (Field field : cls.getFields()) {
            try {
                settings.add(new ParsedSetting<>(field));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!cls.isAnnotationPresent(Category.class)) {
            throw new Exception();
        }
        Category annotation = cls.getAnnotation(Category.class);
        this.displayName = annotation.name().equals("") ? cls.getName() : annotation.name();
    }
}
