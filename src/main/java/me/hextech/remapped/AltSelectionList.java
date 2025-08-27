package me.hextech.remapped;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import me.hextech.remapped.Alt;
import me.hextech.remapped.AltScreen;
import me.hextech.remapped.AltSelectionList_DSrXNkYQoNXcgOtMWUrt;
import me.hextech.remapped.AltSelectionList_MlYuzYrWmNSiQOBPfePW;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.util.Util;

/*
 * Exception performing whole class analysis ignored.
 */
public class AltSelectionList
extends AltSelectionList_MlYuzYrWmNSiQOBPfePW {
    private final AltScreen owner;
    private final MinecraftClient mc;
    private final Alt alt;
    private long lastClickTime;
    final /* synthetic */ AltSelectionList_DSrXNkYQoNXcgOtMWUrt this$0;

    protected AltSelectionList(AltSelectionList_DSrXNkYQoNXcgOtMWUrt this$0, AltScreen ownerIn, Alt alt) {
        this.this$0 = this$0;
        this.owner = ownerIn;
        this.alt = alt;
        this.mc = MinecraftClient.getInstance();
    }

    public Alt getAltData() {
        return this.alt;
    }

    public void method_25343(DrawContext drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        if (hovered) {
            drawContext.fill(x, y, x + entryWidth, y + entryHeight, new Color(255, 255, 255, 100).getRGB());
        }
        TextRenderer textRenderer = this.mc.textRenderer;
        drawContext.drawTextWithShadow(textRenderer, "Username: " + this.alt.getEmail(), x + 32 + 3, y + 2, 0xFFFFFF);
        drawContext.drawTextWithShadow(textRenderer, "Username: " + this.alt.getEmail(), x + 32 + 3, y + 2, 0xFFFFFF);
    }

    public List<? extends Element> method_25396() {
        return Collections.emptyList();
    }

    public List<? extends Selectable> method_37025() {
        return Collections.emptyList();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double d0 = mouseX - (double)this.this$0.method_25342();
        if (d0 <= 32.0 && d0 < 32.0 && d0 > 16.0) {
            this.owner.setSelected(this);
            this.owner.loginToSelected();
            return true;
        }
        this.owner.setSelected(this);
        if (Util.getMeasuringTimeMs() - this.lastClickTime < 250L) {
            this.owner.loginToSelected();
        }
        this.lastClickTime = Util.getMeasuringTimeMs();
        return false;
    }
}
