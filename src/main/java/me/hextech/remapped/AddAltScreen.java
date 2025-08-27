package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.Alt;
import me.hextech.remapped.AltScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class AddAltScreen
extends Screen {
    private final AltScreen parent;
    private TextFieldWidget textFieldAltUsername;

    public AddAltScreen(AltScreen parentScreen) {
        super(Text.of((String)"Alt Manager"));
        this.parent = parentScreen;
    }

    public void method_25426() {
        super.init();
        this.textFieldAltUsername = new TextFieldWidget(this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 2 - 76, 200, 20, Text.of((String)"Enter Name"));
        this.textFieldAltUsername.setText("");
        this.method_37063((Element)this.textFieldAltUsername);
        this.method_37063((Element)ButtonWidget.builder((Text)Text.of((String)"Add Alt"), b -> this.onButtonAltAddPressed()).dimensions(this.field_22789 / 2 - 100, this.field_22790 / 2 + 24, 200, 20).build());
        this.method_37063((Element)ButtonWidget.builder((Text)Text.of((String)"Cancel"), b -> this.onButtonCancelPressed()).dimensions(this.field_22789 / 2 - 100, this.field_22790 / 2 + 46, 200, 20).build());
    }

    public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        drawContext.drawCenteredTextWithShadow(this.field_22793, "Add Alternate Account", this.field_22789 / 2, 20, 0xFFFFFF);
        drawContext.drawCenteredTextWithShadow(this.field_22793, "Username:", this.field_22789 / 2 - 100, this.field_22790 / 2 - 90, 0xFFFFFF);
        super.method_25394(drawContext, mouseX, mouseY, delta);
    }

    private void onButtonAltAddPressed() {
        Alt alt = new Alt(this.textFieldAltUsername.getText());
        HexTech.ALT.addAlt(alt);
        this.parent.refreshAltList();
    }

    public void onButtonCancelPressed() {
        this.field_22787.setScreen((Screen)this.parent);
    }
}
