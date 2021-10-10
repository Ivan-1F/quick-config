package me.ivan1f.quickconfig.setting;

import me.ivan1f.quickconfig.keyboard.MultiKeyBind;

import java.lang.reflect.Field;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class ParsedSetting<T> {
    public boolean withHotkey;
    public MultiKeyBind hotkey;
    public String displayName;
    public final Class<T> type;
    public Consumer<T> onChange;
    public T value;

    public void setOnChange(Consumer<T> onChange) {
        this.onChange = onChange;
    }

    public ParsedSetting(Field field) throws Exception {
        if (!field.isAnnotationPresent(Setting.class)) {
            throw new Exception();
        }
        this.withHotkey = field.isAnnotationPresent(WithHotkey.class);
        if (this.withHotkey) {
            this.hotkey = new MultiKeyBind(field.getAnnotation(WithHotkey.class).hotkey());
        }
        this.displayName = field.getName();
        this.type = (Class<T>) field.getType();
        this.value = (T) field.get(field.getClass());
    }

    public void set(Object value) {
        this.value = (T) value;
        if (this.onChange != null) {
            this.onChange.accept(this.value);
        }
    }
}
