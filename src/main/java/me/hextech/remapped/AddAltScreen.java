package me.hextech.remapped;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class AddAltScreen extends Screen {
   private final AltScreen parent;
   private TextFieldWidget textFieldAltUsername;

   public AddAltScreen(AltScreen parentScreen) {
      super(Text.method_30163("Alt Manager"));
      this.parent = parentScreen;
   }

   public void method_25426() {
      super.method_25426();
      this.textFieldAltUsername = new TextFieldWidget(
         this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 2 - 76, 200, 20, Text.method_30163("Enter Name")
      );
      this.textFieldAltUsername.method_1852("");
      this.method_37063(this.textFieldAltUsername);
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Add Alt"), b -> this.onButtonAltAddPressed())
            .method_46434(this.field_22789 / 2 - 100, this.field_22790 / 2 + 24, 200, 20)
            .method_46431()
      );
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Cancel"), b -> this.onButtonCancelPressed())
            .method_46434(this.field_22789 / 2 - 100, this.field_22790 / 2 + 46, 200, 20)
            .method_46431()
      );
   }

   public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float delta) {
      drawContext.method_25300(this.field_22793, "Add Alternate Account", this.field_22789 / 2, 20, 16777215);
      drawContext.method_25300(this.field_22793, "Username:", this.field_22789 / 2 - 100, this.field_22790 / 2 - 90, 16777215);
      super.method_25394(drawContext, mouseX, mouseY, delta);
   }

   private void onButtonAltAddPressed() {
      Alt alt = new Alt(this.textFieldAltUsername.method_1882());
      me.hextech.HexTech.ALT.addAlt(alt);
      this.parent.refreshAltList();
   }

   public void onButtonCancelPressed() {
      this.field_22787.method_1507(this.parent);
   }
}
