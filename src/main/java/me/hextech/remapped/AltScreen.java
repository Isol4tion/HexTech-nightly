package me.hextech.remapped;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AltScreen extends Screen {
   private final Screen parentScreen;
   private ButtonWidget editButton;
   private ButtonWidget deleteButton;
   private AltSelectionList_DSrXNkYQoNXcgOtMWUrt altListSelector;

   public AltScreen(Screen parentScreen) {
      super(Text.method_30163("Alt Manager"));
      this.parentScreen = parentScreen;
   }

   public void method_25426() {
      super.method_25426();
      this.altListSelector = new AltSelectionList_DSrXNkYQoNXcgOtMWUrt(this, this.field_22787, this.field_22789, this.field_22790, 32, this.field_22790 - 64);
      this.altListSelector.updateAlts();
      this.method_37063(this.altListSelector);
      this.deleteButton = ButtonWidget.method_46430(Text.method_30163("Delete Alt"), b -> this.deleteSelected())
         .method_46434(this.field_22789 / 2 - 154, this.field_22790 - 28, 100, 20)
         .method_46431();
      this.deleteButton.field_22763 = false;
      this.method_37063(this.deleteButton);
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Token Login"), b -> this.field_22787.method_1507(new TokenLoginScreen(this)))
            .method_46434(this.field_22789 / 2 - 154, this.field_22790 - 52, 100, 20)
            .method_46431()
      );
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Direct Login"), b -> this.field_22787.method_1507(new DirectLoginAltScreen(this)))
            .method_46434(this.field_22789 / 2 - 50, this.field_22790 - 52, 100, 20)
            .method_46431()
      );
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Add Alt"), b -> this.field_22787.method_1507(new AddAltScreen(this)))
            .method_46434(this.field_22789 / 2 + 54, this.field_22790 - 52, 100, 20)
            .method_46431()
      );
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Cancel"), b -> this.field_22787.method_1507(this.parentScreen))
            .method_46434(this.field_22789 / 2 + 54, this.field_22790 - 28, 100, 20)
            .method_46431()
      );
      this.editButton = ButtonWidget.method_46430(Text.method_30163("EditionHex Alt"), b -> this.editSelected())
         .method_46434(this.field_22789 / 2 - 50, this.field_22790 - 28, 100, 20)
         .method_46431();
      this.editButton.field_22763 = false;
      this.method_37063(this.editButton);
   }

   public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
      this.altListSelector.method_25394(drawContext, mouseX, mouseY, partialTicks);
      super.method_25394(drawContext, mouseX, mouseY, partialTicks);
      drawContext.method_25300(
         this.field_22793, "Currently Logged Into: " + MinecraftClient.method_1551().method_1548().method_1676(), this.field_22789 / 2, 20, 16777215
      );
   }

   public void method_25419() {
      this.field_22787.method_1507(this.parentScreen);
   }

   public void refreshAltList() {
      this.field_22787.method_1507(new AltScreen(this.parentScreen));
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
      if (altselectionlist$entry != null) {
         Alt alt = ((AltSelectionList)altselectionlist$entry).getAltData();
         me.hextech.HexTech.ALT.loginCracked(alt.getEmail());
      }
   }

   public void editSelected() {
      Alt alt = ((AltSelectionList)this.altListSelector.method_25334()).getAltData();
      if (alt != null) {
         this.field_22787.method_1507(new EditAltScreen(this, alt));
      }
   }

   public void deleteSelected() {
      Alt alt = ((AltSelectionList)this.altListSelector.method_25334()).getAltData();
      if (alt != null) {
         me.hextech.HexTech.ALT.removeAlt(alt);
         this.refreshAltList();
      }
   }
}
