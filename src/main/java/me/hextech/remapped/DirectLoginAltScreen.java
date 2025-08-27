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
        super(Text.method_30163((String)"Direct Login"));
        this.parent = parent;
    }

    public void method_25426() {
        super.method_25426();
        this.textFieldAltUsername = new TextFieldWidget(this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 2 - 76, 200, 20, Text.method_30163((String)"Enter Name"));
        this.method_37063((Element)this.textFieldAltUsername);
        ButtonWidget buttonLoginAlt = ButtonWidget.method_46430((Text)Text.method_30163((String)"Login"), b -> this.onButtonLoginPressed()).method_46434(this.field_22789 / 2 - 100, this.field_22790 / 2 + 24, 200, 20).method_46431();
        this.method_37063((Element)buttonLoginAlt);
        this.method_37063((Element)ButtonWidget.method_46430((Text)Text.method_30163((String)"Cancel"), b -> this.field_22787.method_1507(this.parent)).method_46434(this.field_22789 / 2 - 100, this.field_22790 / 2 + 46, 200, 20).method_46431());
    }

    private void onButtonLoginPressed() {
        HexTech.ALT.loginCracked(this.textFieldAltUsername.method_1882());
        this.field_22787.method_1507(this.parent);
    }

    public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        drawContext.method_25300(this.field_22793, this.field_22785.getString(), this.field_22789 / 2, 20, 0xFFFFFF);
        drawContext.method_25303(this.field_22793, "Enter Username", this.field_22789 / 2 - 100, this.field_22790 / 2 - 90, 0xFFFFFF);
        this.textFieldAltUsername.method_25394(drawContext, mouseX, mouseY, partialTicks);
        super.method_25394(drawContext, mouseX, mouseY, partialTicks);
    }
}
