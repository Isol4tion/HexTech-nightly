package me.hextech.remapped;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TokenLoginScreen extends Screen {
   private final Screen parent;
   private TextFieldWidget textFieldAltName;
   private TextFieldWidget textFieldAltToken;
   private TextFieldWidget textFieldAltUID;

   protected TokenLoginScreen(Screen parent) {
      super(Text.method_30163("Token Login"));
      this.parent = parent;
   }

   public void method_25426() {
      super.method_25426();
      this.textFieldAltName = new TextFieldWidget(
         this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 2 + 4, 200, 20, Text.method_30163("Enter Name")
      );
      this.method_37063(this.textFieldAltName);
      this.textFieldAltToken = new TextFieldWidget(
         this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 2 - 76, 200, 20, Text.method_30163("Enter Token")
      );
      this.textFieldAltToken.method_1880(Integer.MAX_VALUE);
      this.method_37063(this.textFieldAltToken);
      this.textFieldAltUID = new TextFieldWidget(
         this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 2 - 36, 200, 20, Text.method_30163("Enter UID")
      );
      this.textFieldAltUID.method_1880(Integer.MAX_VALUE);
      this.method_37063(this.textFieldAltUID);
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Login"), b -> this.onButtonLoginPressed())
            .method_46434(this.field_22789 / 2 - 100, this.field_22790 / 2 + 24 + 8, 200, 20)
            .method_46431()
      );
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Cancel"), b -> this.field_22787.method_1507(this.parent))
            .method_46434(this.field_22789 / 2 - 100, this.field_22790 / 2 + 46 + 8, 200, 20)
            .method_46431()
      );
   }

   private void onButtonLoginPressed() {
      me.hextech.HexTech.ALT.loginToken(this.textFieldAltName.method_1882(), this.textFieldAltToken.method_1882(), this.textFieldAltUID.method_1882());
      this.field_22787.method_1507(this.parent);
   }

   public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
      drawContext.method_25300(this.field_22793, this.field_22785.getString(), this.field_22789 / 2, 20, 16777215);
      drawContext.method_25303(this.field_22793, "Enter Token", this.field_22789 / 2 - 100, this.field_22790 / 2 - 90, 16777215);
      drawContext.method_25303(this.field_22793, "Enter UUID", this.field_22789 / 2 - 100, this.field_22790 / 2 - 50, 16777215);
      drawContext.method_25303(this.field_22793, "Enter Name", this.field_22789 / 2 - 100, this.field_22790 / 2 - 10, 16777215);
      this.textFieldAltName.method_25394(drawContext, mouseX, mouseY, partialTicks);
      this.textFieldAltToken.method_25394(drawContext, mouseX, mouseY, partialTicks);
      this.textFieldAltUID.method_25394(drawContext, mouseX, mouseY, partialTicks);
      super.method_25394(drawContext, mouseX, mouseY, partialTicks);
   }
}
