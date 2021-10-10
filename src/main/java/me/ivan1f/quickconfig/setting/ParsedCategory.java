package me.ivan1f.quickconfig.setting;

import me.ivan1f.quickconfig.extension.ParsedExtension;
import me.ivan1f.quickconfig.translation.INamedObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParsedCategory implements INamedObject {
    public List<ParsedSetting<?>> settings = new ArrayList<>();
    public String name;
    public String displayName;
    private final ParsedExtension extension;

    public ParsedCategory(Class<?> cls, ParsedExtension extension) throws Exception {
        this.extension = extension;
        Category categoryAnnotation = cls.getAnnotation(Category.class);
        this.name = cls.getSimpleName();
        this.displayName = categoryAnnotation.displayName().equals("") ? this.getTranslationKey() : categoryAnnotation.displayName();

        for (Field field : cls.getFields()) {
            if (field.isAnnotationPresent(Setting.class)) {
                try {
                    this.settings.add(new ParsedSetting<>(field, this));
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
                    if (setting.name.equals(of)) {
                        setting.setOnChange((Consumer) field.get(this));
                    }
                }
            }
        }
        if (!cls.isAnnotationPresent(Category.class)) {
            throw new Exception();
        }
    }

    @Override
    public String getTranslationKey() {
        return this.extension.name + ".category." + this.name;
    }
}
