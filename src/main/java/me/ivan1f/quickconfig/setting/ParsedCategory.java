package me.ivan1f.quickconfig.setting;

import me.ivan1f.quickconfig.translation.INamedObject;
import me.ivan1f.quickconfig.translation.TranslationUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings(value={"unchecked", "rawtypes"})
public class ParsedCategory implements INamedObject {
    public List<ParsedSetting<?>> settings = new ArrayList<>();
    public String name;

    public ParsedCategory(Class<?> cls) throws Exception {
        if (!cls.isAnnotationPresent(Category.class)) {
            throw new Exception();
        }

        this.name = TranslationUtils.lowerFirstCharacter(cls.getSimpleName());

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
    }

    public String getName() {
        return this.getTranslationKey() + ".name";
    }

    @Override
    public String getTranslationKey() {
        return "category." + this.name;
    }
}
