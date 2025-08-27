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
        super(Text.method_30163((String)"Alt Manager"));
        this.parent = parentScreen;
        this.alt = alt;
    }

    public void method_25426() {
        super.method_25426();
        this.textFieldAltUsername = new TextFieldWidget(this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 2 - 76, 200, 20, Text.method_30163((String)"Enter Name"));
        this.textFieldAltUsername.method_1852(this.alt == null ? "" : this.alt.getEmail());
        this.method_37063((Element)this.textFieldAltUsername);
        this.buttonSaveAlt = ButtonWidget.method_46430((Text)Text.method_30163((String)"Save Alt"), b -> this.onButtonAltEditPressed()).method_46434(this.field_22789 / 2 - 100, this.field_22790 / 2 + 24, 200, 20).method_46431();
        this.method_37063((Element)this.buttonSaveAlt);
        this.method_37063((Element)ButtonWidget.method_46430((Text)Text.method_30163((String)"Cancel"), b -> this.onButtonCancelPressed()).method_46434(this.field_22789 / 2 - 100, this.field_22790 / 2 + 46, 200, 20).method_46431());
    }

    public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        drawContext.method_25300(this.field_22793, "Edit Alternate Account", this.field_22789 / 2, 20, 0xFFFFFF);
        drawContext.method_25303(this.field_22793, "Username:", this.field_22789 / 2 - 100, this.field_22790 / 2 - 90, 0xFFFFFF);
        super.method_25394(drawContext, mouseX, mouseY, partialTicks);
    }

    private void onButtonAltEditPressed() {
        this.alt.setEmail(this.textFieldAltUsername.method_1882());
        HexTech.ALT.saveAlts();
        this.parent.refreshAltList();
    }

    public void onButtonCancelPressed() {
        this.field_22787.method_1507((Screen)this.parent);
    }
}
