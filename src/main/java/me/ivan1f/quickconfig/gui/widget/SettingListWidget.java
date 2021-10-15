package me.ivan1f.quickconfig.gui.widget;

import me.ivan1f.quickconfig.gui.ExtensionScreen;
import me.ivan1f.quickconfig.setting.ParsedCategory;
import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.ArrayList;
import java.util.List;

public class SettingListWidget extends ElementListWidget<SettingListWidget.Entry> {
    public SettingListWidget(MinecraftClient minecraftClient, ExtensionScreen screen, ParsedCategory category) {
        super(minecraftClient, screen.width, screen.height, 43, screen.height - 32, 20);
        for (ParsedSetting<?> setting : category.settings) {
            this.addEntry(new SettingEntry(setting, screen));
        }
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width - 10;
    }

    public class SettingEntry extends Entry {
        private final ParsedSetting<?> setting;
        private final ExtensionScreen screen;
        private final List<AbstractButtonWidget> buttons = new ArrayList<>();

        @SuppressWarnings("unchecked")
        public SettingEntry(ParsedSetting<?> setting, ExtensionScreen screen) {
            this.setting = setting;
            this.screen = screen;
            if (this.setting.type == boolean.class) {
                this.buttons.add(new ToggleButton((ParsedSetting<Boolean>) setting));
            } else if (this.setting.type == String.class) {
                this.buttons.add(new TextInputWidget((ParsedSetting<String>) setting));
            }
            if (this.setting.withHotkey) {
                this.buttons.add(new KeyBindButtonWidget(setting, this.screen));
            }
        }

        @Override
        public List<? extends Element> children() {
            return this.buttons;
        }

        @Override
        public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
            TextRenderer textRenderer = SettingListWidget.this.minecraft.textRenderer;
            textRenderer.draw(I18n.translate(this.setting.displayName), 10, y + 5, 16777215);

            int currentX = this.screen.width - 10;
            for (AbstractButtonWidget button : this.buttons) {
                currentX -= button.getWidth() + 5;
                button.x = currentX;
                button.y = y;
                button.render(mouseX, mouseY, delta);
            }
        }
    }

    @Override
    protected void renderHoleBackground(int top, int bottom, int alphaTop, int alphaBottom) {

    }

    public abstract static class Entry extends ElementListWidget.Entry<SettingListWidget.Entry> {

    }
}