package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.Alt;
import me.hextech.remapped.AltScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class EditAltScreen
extends Screen {
    private final AltScreen parent;
    private final Alt alt;
    private ButtonWidget buttonSaveAlt;
    private CheckboxWidget toggleMicrosoft;
    private TextFieldWidget textFieldAltUsername;

    public EditAltScreen(AltScreen parentScreen, Alt alt) {
        super(Text.of((String)"Alt Manager"));
        this.parent = parentScreen;
        this.alt = alt;
    }

    public void init() {
        super.init();
        this.textFieldAltUsername = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 76, 200, 20, Text.of((String)"Enter Name"));
        this.textFieldAltUsername.setText(this.alt == null ? "" : this.alt.getEmail());
        this.addDrawableChild((Element)this.textFieldAltUsername);
        this.buttonSaveAlt = ButtonWidget.builder((Text)Text.of((String)"Save Alt"), b -> this.onButtonAltEditPressed()).dimensions(this.width / 2 - 100, this.height / 2 + 24, 200, 20).build();
        this.addDrawableChild((Element)this.buttonSaveAlt);
        this.addDrawableChild((Element)ButtonWidget.builder((Text)Text.of((String)"Cancel"), b -> this.onButtonCancelPressed()).dimensions(this.width / 2 - 100, this.height / 2 + 46, 200, 20).build());
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        drawContext.drawCenteredTextWithShadow(this.textRenderer, "Edit Alternate Account", this.width / 2, 20, 0xFFFFFF);
        drawContext.drawTextWithShadow(this.textRenderer, "Username:", this.width / 2 - 100, this.height / 2 - 90, 0xFFFFFF);
        super.render(drawContext, mouseX, mouseY, partialTicks);
    }

    private void onButtonAltEditPressed() {
        this.alt.setEmail(this.textFieldAltUsername.getText());
        HexTech.ALT.saveAlts();
        this.parent.refreshAltList();
    }

    public void onButtonCancelPressed() {
        this.client.setScreen((Screen)this.parent);
    }
}
