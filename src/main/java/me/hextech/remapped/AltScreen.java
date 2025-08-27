package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.AddAltScreen;
import me.hextech.remapped.Alt;
import me.hextech.remapped.AltSelectionList;
import me.hextech.remapped.AltSelectionList_DSrXNkYQoNXcgOtMWUrt;
import me.hextech.remapped.AltSelectionList_MlYuzYrWmNSiQOBPfePW;
import me.hextech.remapped.DirectLoginAltScreen;
import me.hextech.remapped.EditAltScreen;
import me.hextech.remapped.TokenLoginScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AltScreen
extends Screen {
    private final Screen parentScreen;
    private ButtonWidget editButton;
    private ButtonWidget deleteButton;
    private AltSelectionList_DSrXNkYQoNXcgOtMWUrt altListSelector;

    public AltScreen(Screen parentScreen) {
        super(Text.of((String)"Alt Manager"));
        this.parentScreen = parentScreen;
    }

    public void method_25426() {
        super.init();
        this.altListSelector = new AltSelectionList_DSrXNkYQoNXcgOtMWUrt(this, this.field_22787, this.field_22789, this.field_22790, 32, this.field_22790 - 64);
        this.altListSelector.updateAlts();
        this.method_37063((Element)this.altListSelector);
        this.deleteButton = ButtonWidget.builder((Text)Text.of((String)"Delete Alt"), b -> this.deleteSelected()).dimensions(this.field_22789 / 2 - 154, this.field_22790 - 28, 100, 20).build();
        this.deleteButton.field_22763 = false;
        this.method_37063((Element)this.deleteButton);
        this.method_37063((Element)ButtonWidget.builder((Text)Text.of((String)"Token Login"), b -> this.field_22787.setScreen((Screen)new TokenLoginScreen(this))).dimensions(this.field_22789 / 2 - 154, this.field_22790 - 52, 100, 20).build());
        this.method_37063((Element)ButtonWidget.builder((Text)Text.of((String)"Direct Login"), b -> this.field_22787.setScreen((Screen)new DirectLoginAltScreen(this))).dimensions(this.field_22789 / 2 - 50, this.field_22790 - 52, 100, 20).build());
        this.method_37063((Element)ButtonWidget.builder((Text)Text.of((String)"Add Alt"), b -> this.field_22787.setScreen((Screen)new AddAltScreen(this))).dimensions(this.field_22789 / 2 + 54, this.field_22790 - 52, 100, 20).build());
        this.method_37063((Element)ButtonWidget.builder((Text)Text.of((String)"Cancel"), b -> this.field_22787.setScreen(this.parentScreen)).dimensions(this.field_22789 / 2 + 54, this.field_22790 - 28, 100, 20).build());
        this.editButton = ButtonWidget.builder((Text)Text.of((String)"EditionHex Alt"), b -> this.editSelected()).dimensions(this.field_22789 / 2 - 50, this.field_22790 - 28, 100, 20).build();
        this.editButton.field_22763 = false;
        this.method_37063((Element)this.editButton);
    }

    public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        this.altListSelector.method_25394(drawContext, mouseX, mouseY, partialTicks);
        super.method_25394(drawContext, mouseX, mouseY, partialTicks);
        drawContext.drawCenteredTextWithShadow(this.field_22793, "Currently Logged Into: " + MinecraftClient.getInstance().getSession().getUsername(), this.field_22789 / 2, 20, 0xFFFFFF);
    }

    public void method_25419() {
        this.field_22787.setScreen(this.parentScreen);
    }

    public void refreshAltList() {
        this.field_22787.setScreen((Screen)new AltScreen(this.parentScreen));
    }

    public void setSelected(AltSelectionList_MlYuzYrWmNSiQOBPfePW selected) {
        this.altListSelector.setSelected(selected);
        this.setEdittable();
    }

    protected void setEdittable() {
        this.editButton.field_22763 = true;
        this.deleteButton.field_22763 = true;
    }

    public void loginToSelected() {
        AltSelectionList_MlYuzYrWmNSiQOBPfePW altselectionlist$entry = (AltSelectionList_MlYuzYrWmNSiQOBPfePW)this.altListSelector.method_25334();
        if (altselectionlist$entry == null) {
            return;
        }
        Alt alt = ((AltSelectionList)altselectionlist$entry).getAltData();
        HexTech.ALT.loginCracked(alt.getEmail());
    }

    public void editSelected() {
        Alt alt = ((AltSelectionList)this.altListSelector.method_25334()).getAltData();
        if (alt == null) {
            return;
        }
        this.field_22787.setScreen((Screen)new EditAltScreen(this, alt));
    }

    public void deleteSelected() {
        Alt alt = ((AltSelectionList)this.altListSelector.method_25334()).getAltData();
        if (alt == null) {
            return;
        }
        HexTech.ALT.removeAlt(alt);
        this.refreshAltList();
    }
}
