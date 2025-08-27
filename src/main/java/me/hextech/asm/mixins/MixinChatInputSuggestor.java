package me.hextech.asm.mixins;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.hextech.HexTech;
import me.hextech.remapped.ChatSetting_qVnAbgCzNciNTevKRovy;
import me.hextech.remapped.Command;
import me.hextech.remapped.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChatInputSuggestor.class})
public abstract class MixinChatInputSuggestor {
   @Final
   @Shadow
   TextFieldWidget field_21599;
   @Shadow
   private CompletableFuture<Suggestions> field_21611;
   @Final
   @Shadow
   private List<OrderedText> field_21607;
   @Unique
   private boolean showOutline = false;

   @Shadow
   public abstract void method_23920(boolean var1);

   @Inject(
      at = {@At("HEAD")},
      method = {"render"}
   )
   private void onRender(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
      if (this.showOutline) {
         int x = this.field_21599.method_46426() - 3;
         int y = this.field_21599.method_46427() - 3;
         Render2DUtil.drawRect(
            context.method_51448(),
            (float)x,
            (float)y,
            (float)(this.field_21599.method_25368() + 1),
            1.0F,
            ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.color.getValue().getRGB()
         );
         Render2DUtil.drawRect(
            context.method_51448(),
            (float)x,
            (float)(y + this.field_21599.method_25364() + 1),
            (float)(this.field_21599.method_25368() + 1),
            1.0F,
            ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.color.getValue().getRGB()
         );
         Render2DUtil.drawRect(
            context.method_51448(),
            (float)x,
            (float)y,
            1.0F,
            (float)(this.field_21599.method_25364() + 1),
            ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.color.getValue().getRGB()
         );
         Render2DUtil.drawRect(
            context.method_51448(),
            (float)(x + this.field_21599.method_25368() + 1),
            (float)y,
            1.0F,
            (float)(this.field_21599.method_25364() + 2),
            ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.color.getValue().getRGB()
         );
      }
   }

   @Inject(
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;getCursor()I",
         ordinal = 0
      )},
      method = {"refresh()V"}
   )
   private void onRefresh(CallbackInfo ci) {
      String prefix = HexTech.PREFIX;
      String string = this.field_21599.method_1882();
      this.showOutline = string.startsWith(prefix);
      if (string.length() > 0) {
         int cursorPos = this.field_21599.method_1881();
         String string2 = string.substring(0, cursorPos);
         if (prefix.startsWith(string2) || string2.startsWith(prefix)) {
            int j = 0;
            Matcher matcher = Pattern.compile("(\\s+)").matcher(string2);

            while (matcher.find()) {
               j = matcher.end();
            }

            SuggestionsBuilder builder = new SuggestionsBuilder(string2, j);
            if (string2.length() < prefix.length()) {
               if (!prefix.startsWith(string2)) {
                  return;
               }

               builder.suggest(prefix);
            } else {
               if (!string2.startsWith(prefix)) {
                  return;
               }

               int count = StringUtils.countMatches(string2, " ");
               List<String> seperated = Arrays.asList(string2.split(" "));
               if (count == 0) {
                  for (Object strObj : HexTech.COMMAND.getCommands().keySet().toArray()) {
                     String str = (String)strObj;
                     builder.suggest(HexTech.PREFIX + str + " ");
                  }
               } else {
                  if (seperated.size() < 1) {
                     return;
                  }

                  Command c = HexTech.COMMAND.getCommandBySyntax(((String)seperated.get(0)).substring(prefix.length()));
                  if (c == null) {
                     this.field_21607.add(Text.method_30163("§cno commands found: §e" + ((String)seperated.get(0)).substring(prefix.length())).method_30937());
                     return;
                  }

                  String[] suggestions = c.getAutocorrect(count, seperated);
                  if (suggestions == null || suggestions.length == 0) {
                     return;
                  }

                  for (String str : suggestions) {
                     builder.suggest(str + " ");
                  }
               }
            }

            this.field_21611 = builder.buildFuture();
            this.method_23920(false);
         }
      }
   }
}
