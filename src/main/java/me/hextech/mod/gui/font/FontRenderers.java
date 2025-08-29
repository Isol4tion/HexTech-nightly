package me.hextech.mod.gui.font;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class FontRenderers {
    public static FontAdapter Arial;
    public static FontAdapter Calibri;
    public static FontAdapter defaul;
    public static FontAdapter ui;

    @NotNull
    public static RendererFontAdapter createDefault(float size, String name) throws IOException, FontFormatException {
        return new RendererFontAdapter(Font.createFont(0, Objects.requireNonNull(FontRenderers.class.getClassLoader().getResourceAsStream("assets/minecraft/font/" + name + ".ttf"))).deriveFont(0, size), size);
    }

    public static void createDefault(float size) {
        try {
            ui = FontRenderers.createDefault(size, "MiSans");
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static RendererFontAdapter createArial(float size) {
        Font font = new Font("tahoma", 0, (int) size);
        return new RendererFontAdapter(font, size);
    }

    public static RendererFontAdapter create(String name, int style, float size) {
        return new RendererFontAdapter(new Font(name, style, (int) size), size);
    }

    public final FontAdapter getDefault() {
        FontAdapter fontAdapter = defaul;
        return fontAdapter;
    }

    public final void setDefault(@NotNull FontAdapter fontAdapter) {
        defaul = fontAdapter;
    }
}
