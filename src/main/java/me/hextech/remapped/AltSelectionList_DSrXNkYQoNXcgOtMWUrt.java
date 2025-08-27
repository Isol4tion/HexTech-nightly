package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.Alt;
import me.hextech.remapped.AltScreen;
import me.hextech.remapped.AltSelectionList;
import me.hextech.remapped.AltSelectionList_MlYuzYrWmNSiQOBPfePW;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.jetbrains.annotations.Nullable;

public class AltSelectionList_DSrXNkYQoNXcgOtMWUrt
extends ElementListWidget<AltSelectionList_MlYuzYrWmNSiQOBPfePW> {
    private final AltScreen owner;
    private final List<AltSelectionList> altList = new ArrayList<AltSelectionList>();

    public AltSelectionList_DSrXNkYQoNXcgOtMWUrt(AltScreen ownerIn, MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.owner = ownerIn;
    }

    public void updateAlts() {
        this.method_25339();
        for (Alt alt : HexTech.ALT.getAlts()) {
            AltSelectionList entry = new AltSelectionList(this, this.owner, alt);
            this.altList.add(entry);
        }
        this.setList();
    }

    private void setList() {
        this.altList.forEach(x$0 -> this.method_25321((EntryListWidget.Entry)x$0));
    }

    public void setSelected(@Nullable AltSelectionList_MlYuzYrWmNSiQOBPfePW entry) {
        super.method_25313((EntryListWidget.Entry)entry);
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        AltSelectionList_MlYuzYrWmNSiQOBPfePW AltSelectionList$entry = (AltSelectionList_MlYuzYrWmNSiQOBPfePW)this.method_25334();
        return AltSelectionList$entry != null && AltSelectionList$entry.method_25404(keyCode, scanCode, modifiers) || super.method_25404(keyCode, scanCode, modifiers);
    }
}
