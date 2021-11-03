package me.ivan1f.quickconfig.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;

public class LabelWidget implements Element {
    private final String message;
    private final int x;
    private final int y;
    private final TextRenderer textRenderer;

    public LabelWidget(String message, int x, int y) {
        this.message = message;
        this.x = x;
        this.y = y;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    public void render(MatrixStack matrices) {
        this.textRenderer.draw(matrices, this.message, x, this.y + 5, 16777215);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        int width = this.textRenderer.getWidth(this.message);
        return mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + width) && mouseY < (double)(this.y + 20);
    }
}
