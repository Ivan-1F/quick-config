package me.ivan1f.quickconfig.gui;

import me.ivan1f.quickconfig.extension.ParsedExtension;
import me.ivan1f.quickconfig.gui.widget.SettingListWidget;
import me.ivan1f.quickconfig.setting.ParsedCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;

import java.util.HashMap;
import java.util.Map;

public class ExtensionScreen extends Screen {
    private final ParsedExtension extension;
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final TextRenderer textRenderer = client.textRenderer;

    private ParsedCategory selected;
    private final Map<ParsedCategory, ButtonWidget> map = new HashMap<>();
    private SettingListWidget listWidget = null;

    public static final int COLOR_WHITE = 0xFFFFFFFF;
    protected static final int LEFT = 8;
    protected static final int TOP = 10;

    public ExtensionScreen(ParsedExtension extension) {
        super(new TranslatableText(extension.displayName));
        this.extension = extension;
        this.selected = this.extension.categories.get(0);
    }

    @Override
    protected void init() {
        int x = 8;
        for (ParsedCategory category : this.extension.categories) {
            int width = textRenderer.getStringWidth(category.displayName) + 8;
            ButtonWidget tabButton = new ButtonWidget(
                    x, 20, width, 20, category.displayName, button -> this.onCategorySelected(category));
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

    public void drawString(String str, int x, int y, int color) {
        super.drawString(textRenderer, str, x, y, color);
    }

    protected void drawTitle() {
        this.drawString(this.title.asFormattedString(), LEFT, TOP, COLOR_WHITE);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.listWidget.render(mouseX, mouseY, delta);
        drawTitle();
        super.render(mouseX, mouseY, delta);
    }
}