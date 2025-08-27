package me.hextech.remapped;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class LogoDrawer {
    public static final Identifier LOGO_TEXTURE;

    public static void draw(DrawContext context, int screenWidth, int screenHeight, float alpha) {
        context.method_51422(1.0f, 1.0f, 1.0f, alpha);
        int i = screenWidth / 2 - 25;
        int o = screenHeight / 4 - 25;
        context.method_25290(LOGO_TEXTURE, i, o, 0.0f, 0.0f, 50, 50, 50, 50);
        context.method_51422(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
