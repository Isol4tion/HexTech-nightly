package me.hextech.remapped;

import me.hextech.HexTech;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class DirectLoginAltScreen
extends Screen {
    private final Screen parent;
    private TextFieldWidget textFieldAltUsername;

    protected DirectLoginAltScreen(Screen parent) {
        super(Text.of((String)"Direct Login"));
        this.parent = parent;
    }

    public void method_25426() {
        super.init();
        this.textFieldAltUsername = new TextFieldWidget(this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 2 - 76, 200, 20, Text.of((String)"Enter Name"));
        this.method_37063((Element)this.textFieldAltUsername);
        ButtonWidget buttonLoginAlt = ButtonWidget.builder((Text)Text.of((String)"Login"), b -> this.onButtonLoginPressed()).dimensions(this.field_22789 / 2 - 100, this.field_22790 / 2 + 24, 200, 20).build();
        this.method_37063((Element)buttonLoginAlt);
        this.method_37063((Element)ButtonWidget.builder((Text)Text.of((String)"Cancel"), b -> this.field_22787.setScreen(this.parent)).dimensions(this.field_22789 / 2 - 100, this.field_22790 / 2 + 46, 200, 20).build());
    }

    private void onButtonLoginPressed() {
        HexTech.ALT.loginCracked(this.textFieldAltUsername.getText());
        this.field_22787.setScreen(this.parent);
    }

    public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        drawContext.drawCenteredTextWithShadow(this.field_22793, this.field_22785.getString(), this.field_22789 / 2, 20, 0xFFFFFF);
        drawContext.drawTextWithShadow(this.field_22793, "Enter Username", this.field_22789 / 2 - 100, this.field_22790 / 2 - 90, 0xFFFFFF);
        this.textFieldAltUsername.method_25394(drawContext, mouseX, mouseY, partialTicks);
        super.method_25394(drawContext, mouseX, mouseY, partialTicks);
    }
}
