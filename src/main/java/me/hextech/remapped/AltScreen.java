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

    public void init() {
        super.init();
        this.altListSelector = new AltSelectionList_DSrXNkYQoNXcgOtMWUrt(this, this.client, this.width, this.height, 32, this.height - 64);
        this.altListSelector.updateAlts();
        this.addDrawableChild(this.altListSelector);
        this.deleteButton = ButtonWidget.builder((Text)Text.of((String)"Delete Alt"), b -> this.deleteSelected()).dimensions(this.width / 2 - 154, this.height - 28, 100, 20).build();
        this.deleteButton.active = false;
        this.addDrawableChild(this.deleteButton);
        this.addDrawableChild(ButtonWidget.builder((Text)Text.of((String)"Token Login"), b -> this.client.setScreen((Screen)new TokenLoginScreen(this))).dimensions(this.width / 2 - 154, this.height - 52, 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder((Text)Text.of((String)"Direct Login"), b -> this.client.setScreen((Screen)new DirectLoginAltScreen(this))).dimensions(this.width / 2 - 50, this.height - 52, 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder((Text)Text.of((String)"Add Alt"), b -> this.client.setScreen((Screen)new AddAltScreen(this))).dimensions(this.width / 2 + 54, this.height - 52, 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder((Text)Text.of((String)"Cancel"), b -> this.client.setScreen(this.parentScreen)).dimensions(this.width / 2 + 54, this.height - 28, 100, 20).build());
        this.editButton = ButtonWidget.builder((Text)Text.of((String)"EditionHex Alt"), b -> this.editSelected()).dimensions(this.width / 2 - 50, this.height - 28, 100, 20).build();
        this.editButton.active = false;
        this.addDrawableChild(this.editButton);
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        this.altListSelector.render(drawContext, mouseX, mouseY, partialTicks);
        super.render(drawContext, mouseX, mouseY, partialTicks);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, "Currently Logged Into: " + MinecraftClient.getInstance().getSession().getUsername(), this.width / 2, 20, 0xFFFFFF);
    }

    public void close() {
        this.client.setScreen(this.parentScreen);
    }

    public void refreshAltList() {
        this.client.setScreen((Screen)new AltScreen(this.parentScreen));
    }

    public void setSelected(AltSelectionList_MlYuzYrWmNSiQOBPfePW selected) {
        this.altListSelector.setSelected(selected);
        this.setEdittable();
    }

    protected void setEdittable() {
        this.editButton.active = true;
        this.deleteButton.active = true;
    }

    public void loginToSelected() {
        AltSelectionList_MlYuzYrWmNSiQOBPfePW altselectionlist$entry = this.altListSelector.getSelectedOrNull();
        if (altselectionlist$entry == null) {
            return;
        }
        Alt alt = ((AltSelectionList)altselectionlist$entry).getAltData();
        HexTech.ALT.loginCracked(alt.getEmail());
    }

    public void editSelected() {
        Alt alt = ((AltSelectionList)this.altListSelector.getSelectedOrNull()).getAltData();
        if (alt == null) {
            return;
        }
        this.client.setScreen((Screen)new EditAltScreen(this, alt));
    }

    public void deleteSelected() {
        Alt alt = ((AltSelectionList)this.altListSelector.getSelectedOrNull()).getAltData();
        if (alt == null) {
            return;
        }
        HexTech.ALT.removeAlt(alt);
        this.refreshAltList();
    }
}
