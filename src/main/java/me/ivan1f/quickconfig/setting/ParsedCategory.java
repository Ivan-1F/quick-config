package me.ivan1f.quickconfig.setting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParsedCategory {
    public List<ParsedSetting<?>> settings = new ArrayList<>();
    public String displayName;

    public ParsedCategory(Class<?> cls) throws Exception {
        for (Field field : cls.getFields()) {
            if (field.isAnnotationPresent(Setting.class)) {
                try {
                    this.settings.add(new ParsedSetting<>(field));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (Field field : cls.getFields()) {
            if (field.isAnnotationPresent(ChangeHandler.class)) {
                ChangeHandler annotation = field.getAnnotation(ChangeHandler.class);
                String of = annotation.of();
                for (ParsedSetting<?> setting : this.settings) {
                    if (setting.displayName.equals(of)) {
                        setting.setOnChange((Consumer) field.get(this));
                    }
                }
            }
        }
        if (!cls.isAnnotationPresent(Category.class)) {
            throw new Exception();
        }
        Category annotation = cls.getAnnotation(Category.class);
        this.displayName = annotation.name().equals("") ? cls.getSimpleName() : annotation.name();
    }
}
