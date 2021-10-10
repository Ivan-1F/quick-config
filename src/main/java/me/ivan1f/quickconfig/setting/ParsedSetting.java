package me.ivan1f.quickconfig.setting;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public class ParsedSetting<T> {
    public boolean withHotkey;
    public String hotkey;
    public String displayName;
    public final Class<T> type;
    public T value;

    public ParsedSetting(Field field) throws Exception {
        if (!field.isAnnotationPresent(Setting.class)) {
            throw new Exception();
        }
        Setting annotation = field.getAnnotation(Setting.class);
        this.withHotkey = field.isAnnotationPresent(WithHotkey.class);
        if (this.withHotkey) {
            this.hotkey = field.getAnnotation(WithHotkey.class).hotkey();
        }
        this.displayName = annotation.name().equals("") ? field.getName() : annotation.name();
        this.type = (Class<T>) field.getType();
        this.value = (T) field.get(field.getClass());
    }

    public void set(Object value) {
        this.value = (T) value;
    }
}
