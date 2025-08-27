package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.jetbrains.annotations.Nullable;

public class AltSelectionList_DSrXNkYQoNXcgOtMWUrt extends ElementListWidget<AltSelectionList_MlYuzYrWmNSiQOBPfePW> {
   private final AltScreen owner;
   private final List<AltSelectionList> altList = new ArrayList();

   public AltSelectionList_DSrXNkYQoNXcgOtMWUrt(AltScreen ownerIn, MinecraftClient minecraftClient, int i, int j, int k, int l) {
      super(minecraftClient, i, j, k, l);
      this.owner = ownerIn;
   }

   public void updateAlts() {
      this.method_25339();

      for (Alt alt : me.hextech.HexTech.ALT.getAlts()) {
         AltSelectionList entry = new AltSelectionList(this, this.owner, alt);
         this.altList.add(entry);
      }

      this.setList();
   }

   private void setList() {
      this.altList.forEach(x$0 -> this.method_25321(x$0));
   }

   public void setSelected(@Nullable AltSelectionList_MlYuzYrWmNSiQOBPfePW entry) {
      super.method_25313(entry);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      AltSelectionList_MlYuzYrWmNSiQOBPfePW AltSelectionList$entry = (AltSelectionList_MlYuzYrWmNSiQOBPfePW)this.method_25334();
      return AltSelectionList$entry != null && AltSelectionList$entry.method_25404(keyCode, scanCode, modifiers)
         || super.method_25404(keyCode, scanCode, modifiers);
   }
}
