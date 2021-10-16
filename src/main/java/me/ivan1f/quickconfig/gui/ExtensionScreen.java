package me.ivan1f.quickconfig.gui;

import me.ivan1f.quickconfig.extension.ParsedExtension;
import me.ivan1f.quickconfig.gui.widget.KeyBindButtonWidget;
import me.ivan1f.quickconfig.gui.widget.SettingListWidget;
import me.ivan1f.quickconfig.keyboard.KeyCodes;
import me.ivan1f.quickconfig.setting.ParsedCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class ExtensionScreen extends Screen {
    private final ParsedExtension extension;
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final TextRenderer textRenderer = client.textRenderer;

    private ParsedCategory selected;
    private final Map<ParsedCategory, ButtonWidget> map = new HashMap<>();
    private SettingListWidget listWidget = null;
    private KeyBindButtonWidget activeKeyBindButton;

    public static final int COLOR_WHITE = 0xFFFFFFFF;
    protected static final int LEFT = 8;
    protected static final int TOP = 10;

    public ExtensionScreen(ParsedExtension extension) {
        super(new LiteralText(I18n.translate(extension.displayName)));
        this.extension = extension;
        this.selected = this.extension.categories.get(0);
    }

    @Override
    protected void init() {
        int x = 8;
        for (ParsedCategory category : this.extension.categories) {
            int width = (int) (textRenderer.getTextHandler().getWidth(I18n.translate(category.displayName)) + 8);
            ButtonWidget tabButton = new ButtonWidget(
                    x, 20, width, 20, new LiteralText(I18n.translate(category.displayName)), button -> this.onCategorySelected(category));
            map.put(category, tabButton);
            x += width + 4;
            this.addButton(tabButton);
        }
        onCategorySelected(this.selected);
    }

    public void onCategorySelected(ParsedCategory category) {
        this.selected = category;
        this.map.values().forEach(btn -> btn.active = true);
        map.get(category).active = false;
        this.children.remove(listWidget);
        this.listWidget = new SettingListWidget(client, this, this.selected);
        this.children.add(this.listWidget);
    }

    public void drawString(MatrixStack matrices, Text str, int x, int y, int color) {
        drawTextWithShadow(matrices, textRenderer, str, x, y, color);
    }

    protected void drawTitle(MatrixStack matrices) {
        this.drawString(matrices, this.title, LEFT, TOP, COLOR_WHITE);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.listWidget.render(matrices, mouseX, mouseY, delta);
        drawTitle(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void setActiveKeyBindButton(KeyBindButtonWidget button) {
        if (this.activeKeyBindButton != null) {
            this.activeKeyBindButton.onClearSelection();
        }

        this.activeKeyBindButton = button;

        if (this.activeKeyBindButton != null) {
            this.activeKeyBindButton.onSelected();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.activeKeyBindButton != null) {
            this.activeKeyBindButton.onKeyPressed(keyCode);
            return true;
        } else {
            if (this.listWidget.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
            if (keyCode == KeyCodes.KEY_ESCAPE) {
                client.openScreen(null);
                this.extension.saveConfig();
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
