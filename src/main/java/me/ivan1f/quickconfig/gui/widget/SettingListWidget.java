package me.ivan1f.quickconfig.gui.widget;

import com.google.common.collect.ImmutableList;
import me.ivan1f.quickconfig.gui.ExtensionScreen;
import me.ivan1f.quickconfig.setting.ParsedCategory;
import me.ivan1f.quickconfig.setting.ParsedSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

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
    protected int getScrollbarPositionX() {
        return this.width - 10;
    }

    public static class SettingEntry extends Entry {
        private final ParsedSetting<?> setting;
        private final ExtensionScreen screen;
        private final List<ClickableWidget> buttons = new ArrayList<>();

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
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int currentX = this.screen.width - 10;
            for (ClickableWidget button : this.buttons) {
                currentX -= button.getWidth() + 5;
                button.x = currentX;
                button.y = y;
                button.render(matrices, mouseX, mouseY, tickDelta);
            }
            LabelWidget label = new LabelWidget(I18n.translate(this.setting.getName()), 10, y);
            label.render(matrices);
            if (label.isMouseOver(mouseX, mouseY) && setting.comment) {
                screen.renderTooltip(matrices, ImmutableList.of(new LiteralText(I18n.translate(this.setting.getComment()))), mouseX, mouseY);
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int k = this.getRowLeft();
        int l = this.top + 4 - (int) this.getScrollAmount();
        this.renderList(matrices, k, l, mouseX, mouseY, delta);
    }

    public abstract static class Entry extends ElementListWidget.Entry<SettingListWidget.Entry> {

    }
}