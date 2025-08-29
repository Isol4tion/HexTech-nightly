package me.hextech.api.alts;

import me.hextech.HexTech;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AltSelectionList_DSrXNkYQoNXcgOtMWUrt
        extends ElementListWidget<AltSelectionList_MlYuzYrWmNSiQOBPfePW> {
    private final AltScreen owner;
    private final List<AltSelectionList> altList = new ArrayList<>();

    public AltSelectionList_DSrXNkYQoNXcgOtMWUrt(AltScreen ownerIn, MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.owner = ownerIn;
    }

    public void updateAlts() {
        this.clearEntries();
        for (Alt alt : HexTech.ALT.getAlts()) {
            AltSelectionList entry = new AltSelectionList(this, this.owner, alt);
            this.altList.add(entry);
        }
        this.setList();
    }

    private void setList() {
        this.altList.forEach(this::addEntry);
    }

    public void setSelected(@Nullable AltSelectionList_MlYuzYrWmNSiQOBPfePW entry) {
        super.setSelected(entry);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        AltSelectionList_MlYuzYrWmNSiQOBPfePW AltSelectionList$entry = this.getSelectedOrNull();
        return AltSelectionList$entry != null && AltSelectionList$entry.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }
}
