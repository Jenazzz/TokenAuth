package org.Main.Rect.tokenauth.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * Logo Renderer
 * Ported to 1.21.5
 * Author: Rect
 */
public class LogoRenderer {
    private final MinecraftClient client;
    private int colorShift = 0;
    
    public LogoRenderer() {
        this.client = MinecraftClient.getInstance();
    }
    
    public void renderHud(DrawContext context) {
        if (client.player == null || client.textRenderer == null) return;
        
        colorShift++;
        if (colorShift > 360) colorShift = 0;
        
        int x = 10;
        int y = 10;
        
        Text madeByText = Text.literal("Made by ");
        Text rectText = Text.literal("Rect").styled(style -> style.withColor(0xFF6B9E));
        
        context.drawTextWithShadow(client.textRenderer, madeByText, x, y, 0xFFFFFF);
        int rectWidth = client.textRenderer.getWidth(madeByText);
        context.drawTextWithShadow(client.textRenderer, rectText, x + rectWidth, y, 0xFF6B9E);
        
        int telegramY = y + 12;
        String telegramText = "t.me/TearsLyn1337";
        
        int color = getGradientColor(colorShift);
        context.drawTextWithShadow(client.textRenderer, telegramText, x, telegramY, color);
    }
    
    private int getGradientColor(int hue) {
        float h = (hue % 360) / 360.0f;
        float s = 0.8f;
        float l = 0.6f;
        
        float c = (1 - Math.abs(2 * l - 1)) * s;
        float x = c * (1 - Math.abs((h * 6) % 2 - 1));
        float m = l - c / 2;
        
        float r = 0, g = 0, b = 0;
        if (h < 1.0f / 6.0f) {
            r = c; g = x; b = 0;
        } else if (h < 2.0f / 6.0f) {
            r = x; g = c; b = 0;
        } else if (h < 3.0f / 6.0f) {
            r = 0; g = c; b = x;
        } else if (h < 4.0f / 6.0f) {
            r = 0; g = x; b = c;
        } else if (h < 5.0f / 6.0f) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }
        
        int red = (int) ((r + m) * 255);
        int green = (int) ((g + m) * 255);
        int blue = (int) ((b + m) * 255);
        
        return (red << 16) | (green << 8) | blue;
    }
}

