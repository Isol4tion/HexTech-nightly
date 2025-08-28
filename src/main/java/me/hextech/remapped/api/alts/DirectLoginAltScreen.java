package me.hextech.remapped.api.alts;

import me.hextech.HexTech;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class DirectLoginAltScreen
extends Screen {
    private final Screen parent;
    private TextFieldWidget textFieldAltUsername;

    protected DirectLoginAltScreen(Screen parent) {
        super(Text.of("Direct Login"));
        this.parent = parent;
    }

    public void init() {
        super.init();
        this.textFieldAltUsername = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 76, 200, 20, Text.of("Enter Name"));
        this.addDrawableChild(this.textFieldAltUsername);
        ButtonWidget buttonLoginAlt = ButtonWidget.builder(Text.of("Login"), b -> this.onButtonLoginPressed()).dimensions(this.width / 2 - 100, this.height / 2 + 24, 200, 20).build();
        this.addDrawableChild(buttonLoginAlt);
        this.addDrawableChild(ButtonWidget.builder(Text.of("Cancel"), b -> this.client.setScreen(this.parent)).dimensions(this.width / 2 - 100, this.height / 2 + 46, 200, 20).build());
    }

    private void onButtonLoginPressed() {
        HexTech.ALT.loginCracked(this.textFieldAltUsername.getText());
        this.client.setScreen(this.parent);
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title.getString(), this.width / 2, 20, 0xFFFFFF);
        drawContext.drawTextWithShadow(this.textRenderer, "Enter Username", this.width / 2 - 100, this.height / 2 - 90, 0xFFFFFF);
        this.textFieldAltUsername.render(drawContext, mouseX, mouseY, partialTicks);
        super.render(drawContext, mouseX, mouseY, partialTicks);
    }
}
