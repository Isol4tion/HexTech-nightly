package me.hextech.remapped;

import me.hextech.HexTech;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TokenLoginScreen
extends Screen {
    private final Screen parent;
    private TextFieldWidget textFieldAltName;
    private TextFieldWidget textFieldAltToken;
    private TextFieldWidget textFieldAltUID;

    protected TokenLoginScreen(Screen parent) {
        super(Text.method_30163((String)"Token Login"));
        this.parent = parent;
    }

    public void init() {
        super.init();
        this.textFieldAltName = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 + 4, 200, 20, Text.method_30163((String)"Enter Name"));
        this.addDrawableChild((Element)this.textFieldAltName);
        this.textFieldAltToken = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 76, 200, 20, Text.method_30163((String)"Enter Token"));
        this.textFieldAltToken.method_1880(Integer.MAX_VALUE);
        this.addDrawableChild((Element)this.textFieldAltToken);
        this.textFieldAltUID = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 36, 200, 20, Text.method_30163((String)"Enter UID"));
        this.textFieldAltUID.method_1880(Integer.MAX_VALUE);
        this.addDrawableChild((Element)this.textFieldAltUID);
        this.addDrawableChild((Element)ButtonWidget.method_46430((Text)Text.method_30163((String)"Login"), b -> this.onButtonLoginPressed()).method_46434(this.width / 2 - 100, this.height / 2 + 24 + 8, 200, 20).method_46431());
        this.addDrawableChild((Element)ButtonWidget.method_46430((Text)Text.method_30163((String)"Cancel"), b -> this.client.method_1507(this.parent)).method_46434(this.width / 2 - 100, this.height / 2 + 46 + 8, 200, 20).method_46431());
    }

    private void onButtonLoginPressed() {
        HexTech.ALT.loginToken(this.textFieldAltName.method_1882(), this.textFieldAltToken.method_1882(), this.textFieldAltUID.method_1882());
        this.client.method_1507(this.parent);
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        drawContext.method_25300(this.textRenderer, this.field_22785.getString(), this.width / 2, 20, 0xFFFFFF);
        drawContext.method_25303(this.textRenderer, "Enter Token", this.width / 2 - 100, this.height / 2 - 90, 0xFFFFFF);
        drawContext.method_25303(this.textRenderer, "Enter UUID", this.width / 2 - 100, this.height / 2 - 50, 0xFFFFFF);
        drawContext.method_25303(this.textRenderer, "Enter Name", this.width / 2 - 100, this.height / 2 - 10, 0xFFFFFF);
        this.textFieldAltName.render(drawContext, mouseX, mouseY, partialTicks);
        this.textFieldAltToken.render(drawContext, mouseX, mouseY, partialTicks);
        this.textFieldAltUID.render(drawContext, mouseX, mouseY, partialTicks);
        super.render(drawContext, mouseX, mouseY, partialTicks);
    }
}
