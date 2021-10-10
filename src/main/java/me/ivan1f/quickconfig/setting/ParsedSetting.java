package me.ivan1f.quickconfig.setting;

import me.ivan1f.quickconfig.keyboard.MultiKeyBind;
import me.ivan1f.quickconfig.translation.INamedObject;

import java.lang.reflect.Field;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class ParsedSetting<T> implements INamedObject {
    public boolean withHotkey;
    public MultiKeyBind hotkey;
    public String name;
    public String displayName;
    public final Class<T> type;
    public Consumer<T> onChange;
    public ParsedCategory category;
    public T value;

    public void setOnChange(Consumer<T> onChange) {
        this.onChange = onChange;
    }

    public ParsedSetting(Field field, ParsedCategory category) throws Exception {
        if (!field.isAnnotationPresent(Setting.class)) {
            throw new Exception();
        }
        Setting annotation = field.getAnnotation(Setting.class);
        this.withHotkey = field.isAnnotationPresent(WithHotkey.class);
        if (this.withHotkey) {
            this.hotkey = new MultiKeyBind(field.getAnnotation(WithHotkey.class).hotkey());
        }
        this.name = field.getName();

        this.category = category;

        if (annotation.displayName().equals("")) {
            this.displayName = this.getTranslationKey();
        } else {
            this.displayName = annotation.displayName();
        }

        this.type = (Class<T>) field.getType();
        this.value = (T) field.get(field.getClass());
    }

    public void set(Object value) {
        this.value = (T) value;
        if (this.onChange != null) {
            this.onChange.accept(this.value);
        }
    }

    @Override
    public String getTranslationKey() {
        return (this.category.getTranslationKey().replace("category", "setting")) + "." + this.name;
    }
}
